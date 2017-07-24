package com.mounthuang.test.con.mthreadpoll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Author: mounthuang
 * Data: 2017/6/24.
 */

/**
 * 线程池最简实现，使用一个线程安全的工作队列连接工作者线程和客户端线程，客户端线程将任务放入工作队列后
 * 便返回，工作者线程不断从工作队列取出任务并执行
 */
public class MSimpleThreadPool<Job extends Runnable> implements MThreadPool<Job> {
    private static final int MAX_WORKER_NUMBERS = 15;
    private static final int DEFAULT_WORKER_NUMBERS = 5;
    private static final int MIN_WORKER_NUMBERS = 1;

    private final LinkedList<Job> jobs = new LinkedList<>();
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
    private int workerNumber = DEFAULT_WORKER_NUMBERS;
    private AtomicLong threadNum = new AtomicLong();

    public MSimpleThreadPool() {
        initWorkers(DEFAULT_WORKER_NUMBERS);
    }

    public MSimpleThreadPool(int num) {
        if (num > MAX_WORKER_NUMBERS) {
            workerNumber = MAX_WORKER_NUMBERS;
        } else if (num < MIN_WORKER_NUMBERS) {
            workerNumber = MIN_WORKER_NUMBERS;
        }
        initWorkers(workerNumber);
    }

    private void initWorkers(int num) {
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker, "ThreadPool-Worker-" + threadNum.incrementAndGet());
            thread.start();
        }

    }

    @Override
    public void execute(Job job) {
        if (job != null) {
            synchronized (jobs) {
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for (Worker worker : workers) {
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        if (num + workerNumber > MIN_WORKER_NUMBERS) {
            num = MAX_WORKER_NUMBERS - workerNumber;
        }
        initWorkers(num);
        workerNumber += num;
    }

    @Override
    public void removeWorker(int num) {
        synchronized (jobs) {
            if (num >= workerNumber) {
                throw new IllegalArgumentException("beyond work numbers");
            }

            int count;
            for (count = 0; count < num; count++) {
                Worker worker = workers.get(count);
                if (workers.remove(worker)) {
                    worker.shutdown();
                }
            }

            workerNumber -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    class Worker implements Runnable {

        private volatile boolean running = true;

        @Override
        public void run() {
            while (running) {
                Job job = null;
                synchronized (jobs) {
                    while (jobs.isEmpty()) {
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    job = jobs.removeFirst();
                }
                if (job != null) {
                    try {
                        job.run();
                    } catch (Exception e) {
                        //just eat it
                    }
                }

            }
        }

        public void shutdown() {
            running = false;
        }
    }
}
