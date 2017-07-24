package com.mounthuang.test.con.mlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * Author: mounthuang
 * Data: 2017/6/24.
 */


public class MMutexTest implements Runnable {
    private final Lock mutex = new MMutex();
    private final Lock lock = new ReentrantLock();
    private static int flag;

    @Override
    public void run() {
        mutex.lock();
        for (int i = 1; i < 10; i++) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(Thread.currentThread().getName());
        }
        mutex.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        MMutexTest mutexTest = new MMutexTest();
        Thread thread1 = new Thread(mutexTest, "A");
        Thread thread2 = new Thread(mutexTest, "B");
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }


}