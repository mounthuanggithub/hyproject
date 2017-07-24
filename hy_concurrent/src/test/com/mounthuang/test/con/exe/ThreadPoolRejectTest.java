package com.mounthuang.test.con.exe;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: mounthuang
 * Data: 2017/7/18.
 */

/**
 * 拒绝策略：
 * AbortPolicy: 默认的阻塞策略，不执行此任务，而且直接抛出一个运行时异常
 * DiscardPolicy: 直接抛弃，任务不执行，空方法
 * CallerRunsPolicy: 在调用execute的线程里面执行此command，会阻塞入口
 * DiscardOldestPolicy: 从队列里面抛弃head的一个任务，并再次execute 此task。
 */
class MyDiscardRejectExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("discard");
    }
}

public class ThreadPoolRejectTest {
    ExecutorService threadPool = new ThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10), new MyDiscardRejectExecutionHandler());

    @Test
    public void testReject() {
        for (int i = 0; i < 13; i++) {
            threadPool.execute(() -> {
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


