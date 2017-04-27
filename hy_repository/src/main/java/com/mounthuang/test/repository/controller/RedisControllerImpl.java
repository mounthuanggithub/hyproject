package com.mounthuang.test.repository.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: mounthuang.
 * Data: 2017/4/24.
 */
@RestController
@RequestMapping("/v1/redisCluster")
@Api(value = "RedisCluster", description = "redis cluster apis")
public class RedisControllerImpl implements RedisController {
    private static final Logger LOG = LoggerFactory.getLogger(RedisControllerImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "set")
    @RequestMapping(value = "/{value}", method = RequestMethod.POST)
    @Override
    public void set(
            @ApiParam(value = "key", required = true) @RequestParam(required = true) String key,
            @ApiParam(value = "value", required = true) @RequestParam(required = true) String value
    ) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set(key, value);
    }

    @ApiOperation(value = "set")
    @RequestMapping(value = "/{value}", method = RequestMethod.GET)
    @Override
    public String get(@ApiParam(value = "key", required = true) @RequestParam(required = true) String key) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        return opsForValue.get(key);
    }

    @Override
    public void delete(String key) {

    }
}
