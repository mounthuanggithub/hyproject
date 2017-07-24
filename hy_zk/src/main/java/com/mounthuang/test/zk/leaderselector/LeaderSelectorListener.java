package com.mounthuang.test.zk.leaderselector;

import com.mounthuang.test.con.core.config.ApplicationConfig;
import com.mounthuang.test.zk.log.ZkLog;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: mounthuang
 * Data: 2017/5/15.
 * <p>
 * 在几个节点之间选举一个leader来跑一个独占服务
 * leaderSelector, 所有存活的client公平轮流做leader
 * 监听本节点是否成功获得leadership，当本节点成为leader后，takeLeaderShip方法被调用，应当阻塞这个方法直到：
 * a.想释放自己的leadership，如节点重启、服务异常、重新发起选举.etc
 * b.zookeeper session异常，如suspended或lost，需要解除阻塞，重新发起选举
 */
@Component
public class LeaderSelectorListener extends LeaderSelectorListenerAdapter {
    private static final Logger logger = ZkLog.getLogger(LeaderSelectorListener.class);
    private static final AtomicBoolean isLeader = new AtomicBoolean(false);
    private static String leaderIpAndPort;
    private String leaderPath;
    private String serverPort;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private CuratorFramework client;

    @PostConstruct
    public void init() {
        leaderPath = applicationConfig.getZkLeaderPath();
        serverPort = applicationConfig.getServerPort();
    }

    public static String getLeaderIpAndPort() {
        return leaderIpAndPort;
    }

    public static AtomicBoolean getIsLeader() {
        return isLeader;
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        logger.info("im in take leader ship");
        this.writeLeaderIpAndPortToZk();
        isLeader.set(true);
//        LeaderShipHookHolder.
        try {
            while (isLeader.get()) {
                this.writeLeaderIpAndPortToZk();
                TimeUnit.SECONDS.sleep(60);

                logger.info("this is leadership {}", leaderIpAndPort);
            }
        } finally {
            isLeader.set(false);
            logger.info("release the leadership");
        }
    }

    private void writeLeaderIpAndPortToZk() throws Exception {
        Stat stat = this.client.checkExists().forPath(leaderPath);
        leaderIpAndPort = InetAddress.getLocalHost().getHostAddress() + ":" + serverPort;
        byte[] ipAndPortBytes = leaderIpAndPort.getBytes();
        if (stat == null) {
            this.client.create().forPath(leaderPath, ipAndPortBytes);
        } else {
            this.client.setData().forPath(leaderPath, ipAndPortBytes);
        }
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 2000)
    public void updateLeaderIpAndPort() {
        try {
            byte[] ipAndPortBytes = this.client.getData().forPath(leaderPath);
            leaderIpAndPort = new String(ipAndPortBytes);
        } catch (Exception e) {
            logger.info("get leaderIpAndPort from zk error.", e);
        }
    }
}

