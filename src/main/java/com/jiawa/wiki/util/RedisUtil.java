package com.jiawa.wiki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * true: 不存在，放入一个key
     * false: 已存在
     * @param key
     * @param second
     * @return
     */
    public boolean validateRepeat(String key,long second){
        if (redisTemplate.hasKey(key)){ //判断redis 中的 key 是否存在
            LOG.info("key已存在,放入:{}",key);
            return false;
        } else { // 在redis中传值考虑过期时间，有容量限制
            LOG.info("key不存在,放入：{}, 过期 {} 秒", key, second);
            redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
            return true;
        }
    }
}
