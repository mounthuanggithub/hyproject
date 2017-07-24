package com.mounthaung.test.con.queue;

import static java.lang.Thread.sleep;

/**
 * Author: mounthuang
 * Data: 2017/7/20.
 */

class Task implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " StartTime:" + System.currentTimeMillis());
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " EndTime:" + System.currentTimeMillis());
    }
}

public class MSimpleBlockingQueueTest {
    private static MSimpleBlockingQueue<Runnable> queue = new MSimpleBlockingQueue<>(5);

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    System.out.println("put thread " + Thread.currentThread().getName() + " working..");
                    queue.put(new Task());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    System.out.println("get thread " + Thread.currentThread().getName() + " working..");
                    Task task = (Task) queue.take();
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }
}