package com.ot.springboot.springbootdemo.controller;

import com.ot.springboot.springbootdemo.anno.Node;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aop")
public class AopController {

    @Autowired
    private AopController aopController;

    @GetMapping("/hello/{msg}")
    public String hello(@PathVariable("msg") String msg){
//        aopController.m(msg);
        ((AopController)AopContext.currentProxy()).m(msg);
//        System.out.println(aopController);
        return msg;
    }

    @Node
    public void m(String msg){
        System.out.println(msg);
    }
}
