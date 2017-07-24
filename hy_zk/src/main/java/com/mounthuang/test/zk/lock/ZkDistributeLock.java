package com.mounthuang.test.zk.lock;

/**
 * Author: mounthuang
 * Data: 2017/7/24.
 */

import com.mounthuang.test.zk.log.ZkLog;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基本原理：利用临时节点与watch机制。每个锁占用一个普通节点/lock，当需要获取锁时在/lock下创建一个临时节点，
 * 创建成功则表示获取锁成功，失败则watch/lock节点，有删除操作后再去争锁。临时节点好处在于当进程挂掉后能自动上锁的节点自动删除即取消锁。
 * <p>
 * 基本原理缺点：所有取锁失败的进程都监听父节点，很容易发生羊群效应，即当释放锁后所有等待进程一起来创建节点，并发量很大
 * <p>
 * 优化:上锁改为创建临时有序节点，每个上锁的节点均能创建节点成功，其序号不同。只有序号最小的可以拥有锁，
 * 当需要不是最小的则watch序号排在前面的一个节点(公平锁)。
 * <p>
 * 步骤：
 * 1. 在/lock节点下创建一个有序临时节点(EPHEMERAL_SEQUENTIAL)。
 * 2. 判断创建的节点序号是否最小，如果是最小则获取锁成功。不是则取锁失败，然后watch序号比本身小的前一个节点。
 * 3. 当取锁失败，设置watch后则等待watch事件到来后，再次判断是否序号最小。
 * 4. 取锁成功则执行代码，最后删除本身节点，释放锁。
 */
public class ZkDistributeLock implements Lock, Watcher {
    private static Logger logger = ZkLog.getLogger(ZkDistributeLock.class);

    private ZooKeeper zk;
    private String root = "/hyproject/locks";
    private String lockName;
    /**
     * 等待前一个锁
     */
    private String waitNode;

    /**
     * 当前锁
     */
    private String myZnode;

    /**
     * 计数器
     */
    private CountDownLatch latch;

    private int sessionTimeout = 3000;

    private List<Exception> exception = new ArrayList<>();

    public ZkDistributeLock(String config, String lockName) {
        this.lockName = lockName;
        try {
            zk = new ZooKeeper(config, sessionTimeout, this);
            Stat stat = zk.exists(root, false);
            if (stat == null) {
                /**
                 * org.apache.zookeeper.CreateMode中定义了四种节点类型，分别对应：
                 * PERSISTENT：永久节点
                 * EPHEMERAL：临时节点
                 * PERSISTENT_SEQUENTIAL：永久节点、序列化
                 * EPHEMERAL_SEQUENTIAL：临时节点、序列化
                 */
                zk.create(root, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * zookeeper节点的监视器
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (this.latch != null) {
            this.latch.countDown();
        }
    }

    @Override
    public void lock() {
        if (exception.size() > 0) {
            throw new LockException(exception.get(0));
        }

        if (this.tryLock()) {
            logger.info("Thread {}", Thread.currentThread().getId());
            return;
        } else {
            try {
                waitForLock(waitNode, sessionTimeout);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        this.lock();
    }

    @Override
    public boolean tryLock() {
        try {
            String splitStr = "_lock_";
            if (lockName.contains(splitStr)) {
                throw new LockException("lockName can not contains '_lock_'");
            }

            // 创建临时子节点
            myZnode = zk.create(root + "/" + lockName + splitStr, new byte[0],
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("{} node created", myZnode);

            // 取出所有子节点
            List<String> subNodes = zk.getChildren(root, false);
            // 取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<>();
            for (String node : subNodes) {
                String _node = node.split(splitStr)[0];
                if (_node.equals(lockName)) {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);
            logger.info("{}=={}", myZnode, lockObjNodes.get(0));
            // 如果是最小节点，则获取锁
            if (myZnode.equals(root + "/" + lockObjNodes.get(0))) {
                return true;
            }
            // 如果不是最小节点，找到比自己小1的节点
            String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
            waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (this.tryLock()) {
            return true;
        }
        try {
            return waitForLock(waitNode, time);
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean waitForLock(String lower, long waitTime) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(root + "/" + lower, true);
        // 判断比自己小一个数的节点是否存在，如果不存在则无需等待锁，同事注册监听
        if (stat != null) {
            logger.info("Thread {} waiting for {}/{}", Thread.currentThread().getId(), root, lower);
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }
        return true;
    }

    @Override
    public void unlock() {
        try {
            logger.info("unlock {}", myZnode);
            zk.delete(myZnode, -1);
            myZnode = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Condition newCondition() {
        return null;
    }

    class LockException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public LockException(String e) {
            super(e);
        }

        public LockException(Exception e) {
            super(e);
        }
    }
}
