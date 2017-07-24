package com.mounthuang.test.con.mlock;

import static java.lang.Thread.sleep;

/**
 * Author: mounthuang
 * Data: 2017/6/24.
 */
public class ThreadTest implements Runnable {
    private static int flag;

    public static int getFlag() {
        return flag;
    }

    public static void clear() {
        flag = 0;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            synchronized (this) {
                flag = flag + 1;
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 10; j++) {
            ThreadTest threadTest = new ThreadTest();
            for (int i = 0; i < 10; i++) {
                Thread thread = new Thread(threadTest);
                thread.start();
            }
            sleep(1000);
            System.out.println(ThreadTest.getFlag());
            ThreadTest.clear();
        }

    }
}
