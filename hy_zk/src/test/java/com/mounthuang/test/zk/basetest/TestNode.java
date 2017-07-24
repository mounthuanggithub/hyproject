package com.mounthuang.test.zk.basetest;

import com.mounthuang.test.zk.lock.ZkDistributeLock;
import org.junit.Test;

/**
 * Author: mounthuang
 * Data: 2017/7/24.
 */
public class TestNode {
    private ZkDistributeLock lock = new ZkDistributeLock("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", "testLock");

    @Test
    public void testSubNode() {
        lock.tryLock();
    }
}
