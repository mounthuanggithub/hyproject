package com.mounthuang.test.con.basetest;

/**
 * Author: mounthuang
 * Data: 2017/7/18.
 */
public class DeadLockTest {

    private String a = "a";
    private String b = "b";

    public void testDeadLockBySync() {
        Thread t1 = new Thread(() -> {
            synchronized (a) {
                System.out.println(Thread.currentThread().getName() + " get object a");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " need b, waiting...");
                synchronized (b) {
                    System.out.println(Thread.currentThread().getName() + " get object b");
                }
            }

        });

        Thread t2 = new Thread(() -> {
            synchronized (b) {
                System.out.println(Thread.currentThread().getName() + " get object b");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " need a, waiting...");
                synchronized (a) {
                    System.out.println(Thread.currentThread().getName() + " get object a");
                }
            }

        });

        t1.start();
        t2.start();
    }

    public void testDeadLockByLock() {
        Thread t1 = new Thread(() -> {

        });

    }


    public static void main(String[] args) {
        DeadLockTest test = new DeadLockTest();
        test.testDeadLockBySync();
    }
}
