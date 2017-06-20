package com.mounthuang.test.con.zk.leaderselector;

import com.mounthuang.test.con.core.config.ApplicationConfig;
import com.mounthuang.test.con.zk.log.ZkLog;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
@Configuration
public class ZkSelectorConfig {
    private static Logger logger = ZkLog.getLogger(ZkSelectorConfig.class);

    @Autowired
    private CuratorFramework client;

    @Autowired
    private LeaderSelectorListener leaderSelectorListener;

    @Autowired
    private ApplicationConfig config;

    @Bean
    public LeaderSelector leaderSelector() {
        String leaderPath = config.getZkLeaderPath();
        logger.info("zk leader path: {}", leaderPath);
        LeaderSelector leaderSelector = new LeaderSelector(client, leaderPath, leaderSelectorListener);
        leaderSelector.start();
        System.out.println("leaderSelector finished");
        return leaderSelector;
    }
}
