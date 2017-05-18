package com.mounthuang.test.core.utils;

import com.mounthuang.test.core.config.ApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
@SpringBootTest(classes = ApplicationConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)

public class ApplicationConfigTest {
    @Autowired
    private ApplicationConfig config;

    @Value("${server.port}")
    private String port;
    @Value("${custom.zk.connectString}")
    private String str;

    @Test
    public void test() {
        System.out.println("serverPort: " + config.getServerPort());
        System.out.println("zkConnectString: " + config.getZkConnectString());
        System.out.println("zkLeaderPath: " + config.getZkLeaderPath());
        System.out.println(str);
        System.out.println(port);
    }
}