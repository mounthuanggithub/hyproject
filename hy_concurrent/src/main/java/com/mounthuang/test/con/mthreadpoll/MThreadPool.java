package com.mounthuang.test.con.mthreadpoll;

/**
 * Author: mounthuang
 * Data: 2017/6/24.
 */
public interface MThreadPool<Job extends Runnable> {
    void execute(Job job);

    void shutdown();

    void addWorkers(int num);

    void removeWorker(int number);

    int getJobSize();
}
