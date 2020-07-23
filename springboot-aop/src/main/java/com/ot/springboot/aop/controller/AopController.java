package com.ot.springboot.aop.controller;

import com.ot.springboot.aop.service.AopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aop")
public class AopController {

    @Autowired
    private AopService service;


    @GetMapping("/hello/{msg}")
    public String hello(@PathVariable("msg") String msg) {
        String hello = service.hello(msg);
        return hello;
    }
}
