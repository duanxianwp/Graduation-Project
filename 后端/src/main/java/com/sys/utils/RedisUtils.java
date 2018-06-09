package com.sys.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtils {

    @Autowired
    private static RedisTemplate redisTemplate;

    public RedisUtils(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static  void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void setExpire(String key, Long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public static void delete(String key){
        redisTemplate.delete(key);
    }

}
