package com.mounthuang.test.con.zk.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: mounthuang
 * Data: 2017/5/15.
 */

public class ZkLog {
    private static Logger logger;
    public static final Logger getLogger(Class cls) {
        logger = LoggerFactory.getLogger(cls);
        return logger;
    }
}

