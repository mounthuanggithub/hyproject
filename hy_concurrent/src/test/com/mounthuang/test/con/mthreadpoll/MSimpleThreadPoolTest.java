package com.mounthuang.test.con.mthreadpoll;

import org.junit.Before;
import org.junit.Test;

/**
 * Author: mounthuang
 * Data: 2017/6/24.
 */
public class MSimpleThreadPoolTest {
    MSimpleThreadPool simpleThreadPool = new MSimpleThreadPool(10);
    Runnable job = () -> {
        for (int i = 0; i < 10000; i++) {
            i++;
        }
    };

    @Before
    public void setUp() {

    }

    public void testSingleThread() {
        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(job);
            thread.start();
        }
    }

    public void testThreadPool() {
        for (int i = 0; i < 10000; i++) {
            simpleThreadPool.execute(job);
        }
    }

    @Test
    public void testTime() {
        Long start = System.currentTimeMillis();
        testSingleThread();
        Long stop = System.currentTimeMillis();
        System.out.println("单线程用时：" + (stop - start));

        start = System.currentTimeMillis();
        testThreadPool();
        stop = System.currentTimeMillis();
        System.out.println("线程池用时：" + (stop - start));
    }
}