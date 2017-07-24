package com.mounthuang.test.zk.leaderselector;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: mounthuang
 * Data: 2017/5/15.
 */
public class LeaderInfo {
    private AtomicBoolean isLeader;
    private String leaderIpAndPort;

    public LeaderInfo() {
    }

    public LeaderInfo(AtomicBoolean isLeader, String leaderIpAndPort) {
        this.isLeader = isLeader;
        this.leaderIpAndPort = leaderIpAndPort;
    }

    public AtomicBoolean getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(AtomicBoolean isLeader) {
        this.isLeader = isLeader;
    }

    public String getLeaderIpAndPort() {
        return leaderIpAndPort;
    }

    public void setLeaderIpAndPort(String leaderIpAndPort) {
        this.leaderIpAndPort = leaderIpAndPort;
    }
}
