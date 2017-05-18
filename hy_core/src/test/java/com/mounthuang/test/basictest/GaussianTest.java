package com.mounthuang.test.basictest;

import org.junit.Test;

import java.util.Random;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
public class GaussianTest {
    @Test
    public void getGaussian() {
        Random random = new Random();
        System.out.println(random.nextGaussian());
    }
}
