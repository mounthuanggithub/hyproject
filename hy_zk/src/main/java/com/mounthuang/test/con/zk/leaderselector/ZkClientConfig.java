package com.mounthuang.test.con.zk.leaderselector;

import com.mounthuang.test.con.core.config.ApplicationConfig;
import com.mounthuang.test.con.zk.log.ZkLog;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: mounthuang
 * Data: 2017/5/17.
 */
@Configuration
public class ZkClientConfig {
    private static Logger logger = ZkLog.getLogger(ZkClientConfig.class);

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean()
    public CuratorFramework curatorFramework() {
        String connectString = applicationConfig.getZkConnectString();
        logger.info("zk connect string: {}", connectString);
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, new RetryNTimes(10, 5000));
        client.start();
        System.out.println("client finished");
        return client;
    }
}
