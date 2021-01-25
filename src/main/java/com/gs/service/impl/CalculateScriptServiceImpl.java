package com.gs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.CalculateScriptTestDTO;
import com.gs.config.Constant;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.service.CalculateScriptService;
import com.gs.service.OPCItemValueRecordService;
import org.apache.commons.lang3.StringUtils;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;
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

    private static PythonInterpreter pyInterpreter = null;

    @Override
    public Double calculateScriptRun(CalculateScriptTestDTO calculateScriptTestDTO) {
        String script = calculateScriptTestDTO.getCalculateScript();
        //提取点位id
        List<String> params = pointParams(calculateScriptTestDTO.getCalculateScript());
        //redis取点位最新值
        for (String param : params) {
            String itemValue = null;
            String s = stringRedisTemplate.opsForValue().get(Constant.REDIS_ITEM_CACHE_PREFIX + calculateScriptTestDTO.getFactoryId() + ":" + param);
            if (StringUtils.isEmpty(s)) {
                List<OPCItemValueRecordEntity> records = opcItemValueRecordService.page(new Page(1, 1), new QueryWrapper<OPCItemValueRecordEntity>().eq("item_id", param).eq("factory_id", calculateScriptTestDTO.getFactoryId()).orderBy(true, false, "item_timestamp")).getRecords();
                if (CollectionUtils.isEmpty(records)) {
                    throw new RuntimeException("点位" + param + "不存在!");
                }
                itemValue = records.get(0).getItemValue();
            } else {
                itemValue = JSONObject.parseObject(s, OPCItemValueRecordEntity.class).getItemValue();
            }
            script = script.replace("${" + param + "}", itemValue);
        }
        //脚本测试
        PythonInterpreter pythonInterpreter = getPythonInterpreter();
        try {
            pythonInterpreter.exec(script);
        } catch (Exception e) {
            throw new RuntimeException("脚本错误,请检查脚本:" + e.getMessage());
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
