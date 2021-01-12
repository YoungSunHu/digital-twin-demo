package com.gs.service.impl;

import com.gs.DTO.CalculateScriptTestDTO;
import com.gs.service.CalculateScriptService;
import org.springframework.stereotype.Service;

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

    @Override
    public Double calculateScriptTest(CalculateScriptTestDTO calculateScriptTestDTO) {
        //提取点位id
        List<String> params = pointParams(calculateScriptTestDTO.getCalculateScript());
        //获取点位参数

        //redis取点位最新值,运行脚本

        return null;
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
