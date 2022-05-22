package org.apache.dolphinscheduler.api.controller.test;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

public class MyTestTemplate {

    /**
     * 遇到什么类型,JUnitSampleTests中定义的什么类型的方法,可以反复调用
     */

    @TestTemplate
    @ExtendWith(JUnitSampleTests.class)
    void testTemplate(Integer parameter) {
        System.out.println(parameter);
    }

    @TestTemplate
    @ExtendWith(JUnitSampleTests.class)
    void testTemplate(String parameter) {
        System.out.println(parameter);
    }

    @TestTemplate
    @ExtendWith(JUnitSampleTests.class)
    void testTemplate(String parameter1, String parameter2) {
        System.out.println(parameter1 + "------" + parameter2);
    }
}
