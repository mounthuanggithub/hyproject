package com.mounthuang.test.con.repository.controller;

/**
 * Author: mounthuang
 * Data: 2017/4/27.
 */
public interface RedisController {
    void set(String key, String value);

    String get(String key);

    void delete(String key);
}
