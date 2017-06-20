package com.mounthuang.test.con.zk.service;

import com.mounthuang.test.con.zk.leaderselector.LeaderSelectorListener;
import com.mounthuang.test.con.zk.leaderselector.LeaderInfo;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
@Service
public class LeaderServiceImpl implements LeaderService {

    @Override
    public LeaderInfo getLeaderInfo() {
        String leaderIpAndPort = LeaderSelectorListener.getLeaderIpAndPort();
        AtomicBoolean isLeader = LeaderSelectorListener.getIsLeader();
        return new LeaderInfo(isLeader, leaderIpAndPort);
    }
}
