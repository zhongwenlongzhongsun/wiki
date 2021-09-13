package com.jiawa.wiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController //返回字符串
// @Controller  // 返回页面
public class TestController {

//    @GetMapping
//    @PutMapping
//    @DeleteMapping
//    @RequestMapping(value = "/user?id=1", method = RequestMethod.GET)
//    @RequestMapping("/hello")
    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }
}
