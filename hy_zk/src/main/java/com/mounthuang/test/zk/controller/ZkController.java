package com.mounthuang.test.zk.controller;

import com.mounthuang.test.zk.leaderselector.LeaderInfo;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
public interface ZkController {
    LeaderInfo getLeaderInfo();
}
