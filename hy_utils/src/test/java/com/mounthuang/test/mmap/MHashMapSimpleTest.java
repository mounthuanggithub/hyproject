package com.mounthuang.test.mmap;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Author: mounthuang
 * Data: 2017/6/20.
 */
public class MHashMapSimpleTest {
    HashMap<String, String> map = new HashMap<>();
    MHashMapSimple<String, String> smap = new MHashMapSimple<>();

    @Test
    public void test() {
        smap.put(null, "null-test");
        smap.put("test-1-key", "test-1-value");
        smap.put("test-2-key", "test-2-value1");
        smap.put("test-2-key", "test-2-value2");
        assertEquals("null-test", smap.get(null));
        assertEquals("test-2-value2", smap.get("test-2-key"));
    }
}