package com.mounthuang.test.mconmap;

/**
 * Author: mounthuang
 * Data: 2017/6/20.
 */

import org.jboss.netty.util.internal.ConcurrentHashMap;

/**
 * 锁分离(锁分段)
 * HashTable 锁整个hash表，ConcurrentHashMap->多个HashTable
 */
public class MCHashMapSimple {
    ConcurrentHashMap<String, String> cmap = new ConcurrentHashMap<>();
}
