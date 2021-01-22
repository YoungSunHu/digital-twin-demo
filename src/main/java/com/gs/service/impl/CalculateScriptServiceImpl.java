package com.gs.service.impl;

import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gs.DTO.CalculateScriptTestDTO;
import com.gs.config.Constant;
import com.gs.dao.entity.OPCItemValueRecordEntity;
import com.gs.service.CalculateScriptService;
import com.gs.service.OPCItemValueRecordService;
import org.apache.commons.lang3.StringUtils;
import org.python.core.PyFloat;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/11 18:07
 * @modified By：
 */
@Service
public class CalculateScriptServiceImpl implements CalculateScriptService {

    private static Pattern paramPattern = Pattern.compile("\\$\\{\\s*(\\w+)\\s*(([\\+\\-])\\s*(\\d+)\\s*)?\\}");

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    OPCItemValueRecordService opcItemValueRecordService;

    @Override
    @Async
    public Double calculateScriptTest(CalculateScriptTestDTO calculateScriptTestDTO) {
        String script = calculateScriptTestDTO.getCalculateScript();
        //提取点位id
        List<String> params = pointParams(calculateScriptTestDTO.getCalculateScript());
        //redis取点位最新值
        params.stream().forEach(
                i -> {
                    String itemValue = null;
                    String s = stringRedisTemplate.opsForValue().get(Constant.REDIS_ITEM_CACHE_PREFIX + calculateScriptTestDTO.getFactoryId() + ":" + i);
                    if (StringUtils.isEmpty(s)) {
                        List records = opcItemValueRecordService.page(new Page(1, 1), new QueryWrapper<OPCItemValueRecordEntity>().eq("item_id", i).eq("factory_id", calculateScriptTestDTO.getFactoryId()).orderBy(true, false, "item_timestamp")).getRecords();
                        if (CollectionUtils.isEmpty(records)) {
                            throw new RuntimeException("点位" + i + "不存在!");
                        }
                        itemValue = JSONObject.parseObject(s, OPCItemValueRecordEntity.class).getItemValue();
                    } else {
                        itemValue = JSONObject.parseObject(s, OPCItemValueRecordEntity.class).getItemValue();
                    }
                    script.replace("${" + i + "}", itemValue);
                }
        );
        //脚本测试
        PythonInterpreter interpreter = new PythonInterpreter();
        try {
            interpreter.exec(script);
        } catch (Exception e) {
            throw new RuntimeException("脚本错误,请检查脚本");
        }
        PyObject result = interpreter.get("result", PyObject.class);
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
}
