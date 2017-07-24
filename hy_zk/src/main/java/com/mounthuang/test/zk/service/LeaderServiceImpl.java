package com.mounthuang.test.zk.service;

import com.mounthuang.test.zk.leaderselector.LeaderInfo;
import com.mounthuang.test.zk.leaderselector.LeaderSelectorListener;
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
