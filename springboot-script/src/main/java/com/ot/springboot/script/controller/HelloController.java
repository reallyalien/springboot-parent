package com.ot.springboot.script.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/1")
    public String remoteTest() {
        System.out.println("1111");
        return "1";
    }
}
