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
        Pattern p = Pattern.compile("");
        Matcher m = p.matcher("result=${_PublicGroup.fhf6.Balance_02_nFlux}+${_PublicGroup.fhf6.Balance_03_nFlux}");
        while (m.find()) {
            String group = m.group();
            System.out.println(m.group().replace("${", "").replace("}", ""));
        }
    }

    @Test
    public void rexTest02() {
        Pattern p = Pattern.compile("MEAN_BY_TIME\\{(.*?)\\}");
        Matcher m = p.matcher("result=${_PublicGroup.fhf6.Balance_02_nFlux}+${_PublicGroup.fhf6.Balance_03_nFlux}");
        while (m.find()) {
            String group = m.group();
            System.out.println(m.group().replace("${", "").replace("}", ""));
        }
    }

}
