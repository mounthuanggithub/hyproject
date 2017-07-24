package com.mounthuang.test.con.mlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Author: mounthuang
 * Data: 2017/6/24.
 * <p>
 * 独占锁最简实现，同一时刻只允许一个线程战友锁
 * 队列同步器实现独占式获取和释放同步状态
 * <p>
 * 独占锁最简实现，同一时刻只允许一个线程战友锁
 * 队列同步器实现独占式获取和释放同步状态
 */

/**
 * 独占锁最简实现，同一时刻只允许一个线程战友锁
 * 队列同步器实现独占式获取和释放同步状态
 */

/**
 * 队列同步器：FIFO双向队列
 * 在获取同步状态时，同步器维护一个同步队列，获取状态失败的线程都会被加入到队列中并在队列中自旋；
 * 移除队列的（或停止自旋）条件是前驱节点为头结点且成功获取了同步状态；
 * 在释放同步状态时，同步器调用tryRelease方法释放同步状态，然后唤醒头节点的后继节点
 *
 */
public class MMutex implements Lock {
    // 队列同步器，推荐被定义为静态内部类
    private static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 独占式获取同步状态，实现该方法需要查询当前状态并判断同步状态是否符合预期，
         * 然后再进行CAS设置同步状态
         *
         * @param acquires
         * @return
         */
        protected boolean tryAcquire(int acquires) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /**
         * 独占式释放同步状态，等待获取同步状态的线程将有机会获取同步状态
         *
         * @param releases
         * @return
         */
        protected boolean tryRelease(int releases) {
            if (getState() == 0) throw new IllegalMonitorStateException();
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        /**
         * 是否处于占用状态
         *
         * @return
         */
        protected boolean isHeldExclusice() {
            return getState() == 1;
        }

        Condition newCondition() {
            return new ConditionObject();
        }

    }

    // lock的操作全部代理到队列同步器
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }

}
