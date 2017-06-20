package com.mounthuang.test.con.basictest;

import java.util.Map;

/**
 * Author: mounthuang
 * Data: 2017/5/16.
 */
public class DataStruct {
    static {
        System.out.println("enter static");
    }

    Map<String, String> testMap;

    public void init() {
        System.out.println("enter init method");
    }

    public Map<String, String> getTestMap() {
        return testMap;
    }

    public void setTestMap(Map<String, String> testMap) {
        this.testMap = testMap;
    }
}
