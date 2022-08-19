package com.jiawa.wiki.controller;

import com.jiawa.wiki.domain.Test;
import com.jiawa.wiki.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController //返回字符串
// @Controller  // 返回页面
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${test.hello:TEST}") //扫描此配置项
    private String testHello;

    @Resource
    private TestService testService;
//    @GetMapping
//    @PutMapping
//    @DeleteMapping
//    @RequestMapping(value = "/user?id=1", method = RequestMethod.GET)
//    @RequestMapping("/hello")
    @GetMapping("/hello")
    public String hello(){
        return "Hello World" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "Hello World Post, " + name;
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable String key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }
}
