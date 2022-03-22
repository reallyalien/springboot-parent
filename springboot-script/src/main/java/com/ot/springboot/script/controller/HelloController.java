package com.ot.springboot.script.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping("/hello")
    public String remoteTest() {
        return "hello,aaaaaaaaaaaaaa";
    }
}
