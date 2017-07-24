//package com.mounthuang.test.con.mlock;
//
//import org.apache.tomcat.jni.Error;
//
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.AbstractQueuedSynchronizer;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReadWriteLock;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
///**
// * Author: mounthuang
// * Data: 2017/6/27.
// */
//
///**
// * 读写锁最简实现，读锁：其他读线程不被阻塞，写线程被阻塞；写锁：其他所有线程被阻塞
// * 支持公平与非公平(默认)
// * 支持重入
// * 支持锁降级：遵循获取写锁、获取读锁再释放写锁次序；写锁可以降级为读锁
// * <p>
// * 核心： 一个排它锁(写锁),一个共享锁(读锁)
// */
//public class MReentrantReadWriteLock implements ReadWriteLock {
//
//    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//
//    private final MReentrantReadWriteLock.MReadLock readerLock;
//    private final MReentrantReadWriteLock.MWriteLock writerLock;
//    final Sync sync;
//
//    MReentrantReadWriteLock(boolean fair) {
//        sync = fair ? new FairSync() : new NonfairSync();
//        readerLock = new MReadLock();
//        writerLock = new MWriteLock();
//    }
//
//    MReentrantReadWriteLock() {
//        // 默认非公平锁
//        this(false);
//    }
//
//    abstract static class Sync extends AbstractQueuedSynchronizer {
//        static final int SHARED_SHITF = 16;
//        static final int SHARED_UNIT = (1 << SHARED_SHITF);
//        static final int MAX_COUNT = (1 << SHARED_SHITF) - 1;
//        static final int EXCLUSIVE_MASK = (1 << SHARED_SHITF) - 1;
//
//        // 获取当前共享锁数量
//        static int sharedCount(int c) {
//            return c >>> SHARED_SHITF;
//        }
//
//        // 获取当前排它锁数量
//        static int exclusiveCount(int c) {
//            return c & EXCLUSIVE_MASK;
//        }
//
//        protected final boolean tryAcquire(int acquires) {
//            Thread current = Thread.currentThread();
//            int c = getState();
//            int w = exclusiveCount(c);
//            if (c != 0) {
//                // 存在读锁或者当前获取线程不是已经获取写锁的线程
//                if (w == 0 || current != getExclusiveOwnerThread()) {
//                    return false;
//                }
//
//                if (w + exclusiveCount(acquires) > MAX_COUNT) {
//                    throw new Error("Maximum lock count exceeded");
//                }
//
//                setState(acquires);
//                return true;
//            }
//        }
//
//        protected final int tryAcquireShared(int unused) {
//            for (; ; ) {
//                int c = getState();
//                int nextc = c + (1 << 16);
//                if (nextc < 0) {
//                    throw new Error("Maximum lock count exceeded");
//                }
//                // 如果已有排它锁，则获取共享锁失败
//                if (exclusiveCount(c) != 0 && Thread.currentThread() != getExclusiveOwnerThread()) {
//                    return -1;
//                }
//                if (compareAndSetState(c, nextc)) {
//                    return 1;
//                }
//
//            }
//        }
//    }
//
//    static final class NonfairSync extends Sync {
//
//    }
//
//    static final class FairSync extends Sync {
//
//    }
//
//    static class MReadLock implements Lock {
//        private final Sync sync;
//
//        protected MWriteLock(MReentrantReadWriteLock lock) {
//            sync = lock.sync;
//        }
//
//        @Override
//        public void lock() {
//            sync.acquireShared(1);
//        }
//
//        @Override
//        public void lockInterruptibly() throws InterruptedException {
//
//        }
//
//        @Override
//        public boolean tryLock() {
//            return false;
//        }
//
//        @Override
//        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//            return false;
//        }
//
//        @Override
//        public void unlock() {
//            sync.release(1);
//        }
//
//        @Override
//        public Condition newCondition() {
//            return null;
//        }
//
//        MReadLock(Sync sync) {
//            this.sync = sync;
//        }
//    }
//
//    /**
//     * 写锁，一个支持重入的排它锁；
//     * 如果当前线程已经获取写锁，则增加写状态
//     * 如果当前线程在获取写锁时，读锁已经被获取活该线程不是已经获取写锁的线程，则当前线程进入等待
//     */
//    static class MWriteLock implements Lock {
//        private final Sync sync;
//
//        MWriteLock(MReentrantReadWriteLock lock) {
//            sync = lock.sync;
//        }
//
//        @Override
//        public void lock() {
//            sync.acquire(1);
//        }
//
//        @Override
//        public void lockInterruptibly() throws InterruptedException {
//
//        }
//
//        @Override
//        public boolean tryLock() {
//            return false;
//        }
//
//        @Override
//        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
//            return false;
//        }
//
//        @Override
//        public void unlock() {
//            sync.release(1);
//        }
//
//        @Override
//        public Condition newCondition() {
//            return null;
//        }
//    }
//
//    @Override
//    public Lock readLock() {
//        return readerLock;
//    }
//
//    @Override
//    public Lock writeLock() {
//        return writerLock;
//    }
//}
