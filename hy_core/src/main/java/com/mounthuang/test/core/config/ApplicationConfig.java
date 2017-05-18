package com.mounthuang.test.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
@Component
public class ApplicationConfig {
    @Value("${server.port}")
    private String serverPort;
    @Value("${custom.zk.connectString}")
    private String zkConnectString;
    @Value("${custom.zk.leaderPath}")
    private String zkLeaderPath;

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getZkConnectString() {
        return zkConnectString;
    }

    public void setZkConnectString(String zkConnectString) {
        this.zkConnectString = zkConnectString;
    }

    public String getZkLeaderPath() {
        return zkLeaderPath;
    }

    public void setZkLeaderPath(String zkLeaderPath) {
        this.zkLeaderPath = zkLeaderPath;
    }
}
