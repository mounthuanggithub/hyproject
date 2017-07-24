package com.mounthuang.test.con.mlock;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Author: mounthuang
 * Data: 2017/6/27.
 */

/**
 * 可冲入锁的最简实现
 */
public class MReentrantLock implements Lock {

    abstract static class Sync extends AbstractQueuedSynchronizer {

        /**
         * 非公平锁，只要CAS设置同步状态成功，则表示当前线程获取了锁
         *
         * @param acquires
         * @return
         */
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) { // 重入上限是int最大值？
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }

            return false;
        }

        /**
         * 公平锁，区别为加入了同步队列中当前节点是否有前驱节点的判断，如果有前驱节点，表示有线程比当前线程更早请求获取锁，
         *
         * @param acquires
         * @return
         */
        final boolean FairTryAcquire(int acquires) {
            Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new IllegalMonitorStateException("Maxium lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }

        /**
         * 如果锁被重入了n次，那么前n-1次释放锁都返回false，最后一次释放返回true
         *
         * @param releases
         * @return
         */
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        abstract void lock();

        final boolean isLocked() {
            return getState() == 0;
        }

        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        final Thread getOwner() {
            return getState() == 0 ? null : Thread.currentThread();
        }

        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }
    }

    static final class NonfairSync extends Sync {
        final void lock() {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                acquire(1);
            }
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    static final class FairSync extends Sync {
        final void lock() {
            acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return FairTryAcquire(acquires);
        }
    }


    private final Sync sync;

    public MReentrantLock() {
        sync = new NonfairSync();
    }

    public MReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    public final boolean isFair() {
        return sync.isLocked();
    }

    public void lock() {
        sync.lock();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public boolean tryLock() {
        return sync.nonfairTryAcquire(1);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    public void unlock() {
        sync.release(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public Thread getOwner() {
        return sync.getOwner();
    }

    protected Collection<Thread> getQueuedThreads() {
        return sync.getQueuedThreads();
    }
}
