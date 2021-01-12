package com.gs.test;

import com.gs.DigitalTwinDemoAppllication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：YoungSun
 * @date ：Created in 2021/1/12 9:25
 * @modified By：
 */
@SpringBootTest(classes = DigitalTwinDemoAppllication.class)
@RunWith(SpringRunner.class)
public class PatterTest {

    @Test
    public void rexTest() {
        Pattern p = Pattern.compile("\\$\\{\\s*(\\w+)\\s*(([\\+\\-])\\s*(\\d+)\\s*)?\\}");
        Matcher m = p.matcher("${1},${2},${3},${4},${5}");
        while (m.find()) {
            String group = m.group();
            System.out.println(m.group().replace("${", "").replace("}", ""));
        }
    }

}
