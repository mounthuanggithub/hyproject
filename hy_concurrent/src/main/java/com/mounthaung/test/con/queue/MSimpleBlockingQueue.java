package com.mounthaung.test.con.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author: mounthuang
 * Data: 2017/7/20.
 */

/**
 * 阻塞队列：队列是空的时,从队列中获取元素的操作将会被阻塞；当队列是满时,往队列里添加元素的操作会被阻塞
 * <p>
 * 生产者消费者模式
 *
 * @param <E>
 */
public class MSimpleBlockingQueue<E> {
    private LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();

    /**
     * 队列容量
     */
    private static final int DEFAULT_CAPACITY = 128;

    /**
     * 队列初始容量
     */
    private int capacity;

    /**
     * 队列元素数组，循环队列
     */
    private E[] queue;

    /**
     * 队列中元素个数
     */
    private int size;

    /**
     * 队头索引
     */
    private int head;

    /**
     * 队尾索引
     */
    private int tail;

    /**
     * 锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 存元素condition
     */
    private Condition notFull = lock.newCondition();

    /**
     * 取元素condition
     */
    private Condition notEmpty = lock.newCondition();

    public MSimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
        queue = (E[]) new Object[capacity];
    }

    public MSimpleBlockingQueue() {
        this(DEFAULT_CAPACITY);
    }

    public MSimpleBlockingQueue(Collection<E> c, int capacity) {
        this(capacity);
        if (capacity < c.size()) {
            throw new IllegalArgumentException("初始集合大于队列容量");
        }
        Iterator<E> iter = c.iterator();
        while (iter.hasNext()) {
            queue[tail++] = iter.next();
            ++size;
        }
    }

    /**
     * 添加元素到队列尾，若队列已满，该方法阻塞
     */
    public void put(E element) throws InterruptedException {
        lock.lock();
        try {
            while (size >= capacity) {
                notFull.await();
            }
            queue[tail] = element;
            if (++tail == capacity) {
                tail = 0;
            }
            ++size;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从队头移除一个元素，并返回该元素，如果队列为空，将阻塞知道有元素为止
     */
    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                notEmpty.await();
            }
            E value = queue[head];
            queue[head] = null;
            if (++head == capacity) {
                head = 0;
            }
            --size;
            notFull.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }
}
