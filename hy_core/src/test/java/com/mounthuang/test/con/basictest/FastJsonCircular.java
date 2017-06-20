package com.mounthuang.test.con.basictest;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: mounthuang
 * Data: 2017/5/16.
 */
public class FastJsonCircular {
    @Test
    public void circularTest() {
        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("key1", "value1");
        testMap.put("key2", "value2");
        DataStruct d1 = new DataStruct();
        d1.setTestMap(testMap);

        DataStruct d2 = new DataStruct();
        d2.setTestMap(testMap);

        List<DataStruct> dataStructList = new ArrayList<DataStruct>();
        dataStructList.add(d1);
        dataStructList.add(d2);
        System.out.println(JSON.toJSONString(dataStructList));
    }
}
