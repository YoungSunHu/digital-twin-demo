package com.gs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.CalculateScriptTestDTO;
import com.gs.config.Constant;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.dao.entity.TwinPointEntity;
import com.gs.dao.entity.TwinPointValueRecordEntity;
import com.gs.dao.mapper.TwinPointMapper;
import com.gs.dao.mapper.TwinPointValueRecordMapper;
import com.gs.exception.BussinessException;
import com.gs.service.CalculateScriptService;
import com.gs.service.OPCItemValueRecordService;
import com.gs.service.TwinPointService;
import org.apache.commons.lang3.StringUtils;
import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 18:07
 * @modified By：
 */
@Service
public class CalculateScriptServiceImpl implements CalculateScriptService {

    private static Pattern paramPattern = Pattern.compile("\\$\\{(.*?)\\}");

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Autowired
    TwinPointMapper twinPointMapper;

    @Autowired
    TwinPointValueRecordMapper twinPointValueRecordMapper;

    private static PythonInterpreter pyInterpreter = null;

    @Override
    public Double calculateScriptRun(CalculateScriptTestDTO calculateScriptTestDTO) {
        String script = calculateScriptTestDTO.getCalculateScript();
        //提取点位id
        List<String> params = pointParams(calculateScriptTestDTO.getCalculateScript());
        //redis取点位最新值
        for (String param : params) {
            String itemValue = null;
            //获取孪生点位数值
            //String s = stringRedisTemplate.opsForValue().get(Constant.REDIS_ITEM_CACHE_PREFIX + calculateScriptTestDTO.getFactoryId() + ":" + param);
            String s = stringRedisTemplate.opsForValue().get(Constant.REDIS_TWIN_POINT_CACHE_PREFIX + calculateScriptTestDTO.getFactoryId() + ":" + param);
            if (StringUtils.isEmpty(s)) {
                //List<OPCItemValueRecordEntity> records = opcItemValueRecordService.page(new Page(1, 1), new QueryWrapper<OPCItemValueRecordEntity>().eq("item_id", param).eq("factory_id", calculateScriptTestDTO.getFactoryId()).orderBy(true, false, "item_timestamp")).getRecords();
                TwinPointEntity twinPointEntity = twinPointMapper.selectOne(new QueryWrapper<TwinPointEntity>().eq("point_code", param).eq("production_line_id", calculateScriptTestDTO.getProductionLineId()).eq("factory_id", calculateScriptTestDTO.getFactoryId()));
                if (twinPointEntity == null) {
                    throw new BussinessException("点位" + param + "不存在!");
                }
                itemValue = twinPointEntity.getPointValue();
            } else {
                itemValue = JSONObject.parseObject(s, OPCItemValueRecordEntity.class).getItemValue();
            }
            script = script.replace("${" + param + "}", itemValue);
        }
        //脚本函数处理
        script = functionHandler(script, calculateScriptTestDTO.getFactoryId(), calculateScriptTestDTO.getProductionLineId());
        //脚本测试
        PythonInterpreter pythonInterpreter = getPythonInterpreter();
        try {
            pythonInterpreter.exec(script);
        } catch (PyException e) {
            throw new BussinessException("脚本错误,请检查脚本:" + e.value.asString());
        }
        PyObject result = pythonInterpreter.get("result", PyObject.class);
        return result.asDouble();
    }

    /**
     * 提取脚本中的点位参数
     *
     * @param script
     * @return
     */
    private static List<String> pointParams(String script) {
        List<String> params = new ArrayList<>();
        Matcher matcher = paramPattern.matcher(script);
        while (matcher.find()) {
            params.add(matcher.group().replace("${", "").replace("}", ""));
        }
        return params;
    }


    /**
     * 脚本函数处理
     *
     * @param script
     * @return
     */
    private String functionHandler(String script, String factoryId, String productionLineId) {
        //MEAN_BY_TIME 函数处理 MEAN_BY_TIME[WIQ_5101, {h}:{m}, {d}]
        Pattern paramPattern = Pattern.compile(Constant.SCRIPT_FUNCTION_RE_MEAN_BY_TIME);
        Matcher matcher = paramPattern.matcher(script);
        while (matcher.find()) {
            String group = matcher.group();
            String[] split = group.replace("MEAN_BY_TIME[", "").replace("]", "").split(",");
            //点位code
            String pointCode = split[0];
            //{h}:{m}
            String hour = split[1].split(":")[0].replace("{", "").replace("}", "");
            String minute = split[1].split(":")[1].replace("{", "").replace("}", "");
            //{d}
            String day = split[2].replace("{", "").replace("}", "");
            //查询时间,获取时间点最近的点位数值
            LocalDateTime checkTime = LocalDateTime.now().minus(Integer.valueOf(day), ChronoUnit.DAYS).withHour(Integer.valueOf(hour)).withMinute(Integer.valueOf(minute));
            QueryWrapper<TwinPointValueRecordEntity> queryWrapper = new QueryWrapper<TwinPointValueRecordEntity>()
                    .eq("factory_id", factoryId)
                    .eq("twin_point_code", pointCode)
                    .eq("production_line_id", productionLineId)
                    .lt("create_time", checkTime)
                    .orderBy(true, false, "create_time");
            IPage<TwinPointValueRecordEntity> page = twinPointValueRecordMapper.selectPage(new Page<>(1, 1), queryWrapper);
            if (CollectionUtils.isEmpty(page.getRecords())) {
                throw new BussinessException("脚本函数:MEAN_BY_TIME处理异常:点位" + pointCode + "在时间点:" + checkTime + "未查询到对应值");
            }
            script = script.replace(group, page.getRecords().get(0).getTwinPointValue());
        }
        return script;
    }

    private static PythonInterpreter getPythonInterpreter() {
        if (pyInterpreter == null) {
            Properties props = new Properties();
            props.put("python.home", "../jython-2.7.0");
            props.put("python.console.encoding", "UTF-8");
            props.put("python.security.respectJavaAccessibility", "false");
            props.put("python.import.site", "false");
            Properties preprops = System.getProperties();
            PythonInterpreter.initialize(preprops, props, new String[0]);
            pyInterpreter = new PythonInterpreter();
            pyInterpreter.exec("import sys");
            pyInterpreter.exec("print 'prefix', sys.prefix");
            pyInterpreter.exec("print sys.path");
            System.out.println("python的jar包引用正确");
            pyInterpreter = new PythonInterpreter();
        }
        return pyInterpreter;
    }
}
