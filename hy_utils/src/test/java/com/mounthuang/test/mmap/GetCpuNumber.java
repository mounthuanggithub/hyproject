package com.mounthuang.test.mmap;

import org.junit.Test;

import java.io.IOException;

/**
 * Author: mounthuang
 * Data: 2017/7/12.
 */
public class GetCpuNumber {
    @Test
    public void getCpuNum() throws IOException {
        System.out.println("cpu number: " + Runtime.getRuntime().availableProcessors());
    }
}
