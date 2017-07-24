package com.mounthuang.test.con.exe;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: mounthuang
 * Data: 2017/7/16.
 */
public class ThreadPoolTest {

    /**
     * corePoolSize = 0; maximumPoolSize = Integer.MAX_VALUE; KeepAliveTime=60s; SynchronousQueue;
     * 耗时较短的任务
     * 任务处理速度>任务提交速度,才能不断创建新的线程，在任务执行时间无限延长的极端情况下会创建过多的线程
     */
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @Test
    public void testCachedThreadPool() throws InterruptedException {
        // 任务处理速度<任务提交速度, 将只有1个线程
        for (int i = 0; i < 10; i++) {
            Thread.sleep(100);
            cachedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        //任务处理速度>任务提交速度，会不断创建新线程
        for (int i = 0; i < 10; i++) {
            cachedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * corePoolSize = 设定参数； KeepAliveTime=0(多余线程立即终止)；LinkedBlockingQueue;
     * LinkedBlockingQueue: 无界队列，maximumPoolSize是无效参数
     * KeepAliveTime为无效参数
     * 不会拒绝任务，当处理任务无限等待的时候会造成内存问题
     */
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    @Test
    public void testFixedThreadPool() {
        // 创建线程数不会超过corePoolSize
        for (int i = 0; i < 20; i++) {
            System.out.println(i);
            fixedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    /**
     * corePoolSize = maximumPoolSize = 1; LinkedBlockingQueue
     * 只有一个线程！适用于在逻辑上需要单线程处理的任务
     * 不会拒绝任务，当处理任务无限等待的时候会造成内存问题
     */
    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();

    @Test
    public void testSingleThreadPool() {
        // 创建线程数不会超过corePoolSize
        for (int i = 0; i < 20; i++) {
            singleThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}