package com.mounthuang.test.con.core.utils;

import com.mounthuang.test.con.core.config.ApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    public void test() {
        System.out.println("serverPort: " + config.getServerPort());
        System.out.println("zkConnectString: " + config.getZkConnectString());
        System.out.println("zkLeaderPath: " + config.getZkLeaderPath());
    }
}