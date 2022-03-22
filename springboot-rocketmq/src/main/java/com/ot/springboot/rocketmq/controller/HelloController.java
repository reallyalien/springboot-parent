package com.ot.springboot.rocketmq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangtao
 */
@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello,bbbbbbbbbbbbbbbb";
    }

}
