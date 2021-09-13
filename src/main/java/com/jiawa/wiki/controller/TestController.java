package com.jiawa.wiki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController //返回字符串
// @Controller  // 返回页面
public class TestController {

    @Value("${test.hello}") //扫描此配置项
    private String testHello;
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
}
