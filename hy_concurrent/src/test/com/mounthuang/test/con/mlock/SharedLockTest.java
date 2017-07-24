package com.mounthuang.test.con.mlock;

import org.junit.Test;

import java.util.concurrent.locks.Lock;

import static java.lang.Thread.sleep;

/**
 * Author: mounthuang
 * Data: 2017/6/26.
 */
public class SharedLockTest {
    @Test
    public void test() throws InterruptedException {
        final Lock lock = new SharedLock();
        class Worker extends Thread {
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        for (int i = 0; i < 10; i++) {
                            System.out.print(Thread.currentThread().getName());
                            sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println();
                        lock.unlock();
                    }
                }
            }
        }
        Worker worker = new Worker();
        Thread thread1 = new Thread(worker, "A");
        Thread thread2 = new Thread(worker, "B");
        Thread thread3 = new Thread(worker, "C");
        thread1.start();
        thread2.start();
        thread3.start();
        sleep(100);
    }
}