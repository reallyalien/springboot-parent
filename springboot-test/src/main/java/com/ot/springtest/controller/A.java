package com.ot.springtest.controller;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class A {

    @Autowired
    private A a;

    @RequestMapping("/a")
    public String a(){
        System.out.println("a方法进来----");
        System.out.println("当前线程："+Thread.currentThread().getName());
        System.out.println("a方法结束----");
        return "a";

    }
    @Async
    public void b(){
        System.out.println("b方法进来----");
        System.out.println("当前线程："+Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("b方法结束----");
    }
}
