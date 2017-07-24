package com.mounthuang.test.con.mlock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Author: mounthuang
 * Data: 2017/6/27.
 */
public class MReentrantLockTest {
    private static MReentrantLock fairLock = new MReentrantLock(true);
    private static MReentrantLock nonfairLock = new MReentrantLock();

    @Test
    public void fair() throws InterruptedException {
        System.out.println("fair reentrant lock: ");
        testLock(fairLock);
    }

    @Test
    public void nonfair() throws InterruptedException {
        System.out.println("nonfair reentrant lock: ");
        testLock(nonfairLock);
    }

    private void testLock(MReentrantLock lock) throws InterruptedException {
        Job job = new Job(lock);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(job);
            threads.add(thread);
            thread.start();
        }
        sleep(30);
    }

    private static class Job extends Thread {
        private MReentrantLock lock;

        public Job(MReentrantLock lock) {
            this.lock = lock;
        }

        public void run() {
            try {
                lock.lock();
                try {
                    sleep(1);
                    lock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
                System.out.println("Lock by:[ " + Thread.currentThread().getName() + " ]");
                Collection<Thread> collection = lock.getQueuedThreads();
                System.out.print("Wait by:[ ");
                for (Thread thread : collection) {
                    System.out.print(thread.getName() + " ");
                }
                System.out.print("]");
                System.out.println();

            } finally {
                lock.unlock();
            }
        }
    }
}