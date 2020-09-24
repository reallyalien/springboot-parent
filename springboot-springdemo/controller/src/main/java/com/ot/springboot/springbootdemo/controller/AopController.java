package com.ot.springboot.springbootdemo.controller;

import com.ot.springboot.springbootdemo.anno.Node;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aop")
public class AopController {

    //    @Autowired
//    private AopController aopController;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/hello/{msg}")
    public String hello(@PathVariable("msg") String msg) {
        System.out.println("controller线程：" + Thread.currentThread().getName());
//        aopController.m(msg);
        AopController o = (AopController) AopContext.currentProxy();
        String m = o.m(msg);
        System.out.println("controller线程：" + "执行结束");
        return m;
    }

    @Node
    public String m(String msg) {
        System.out.println("m方法执行了"+Thread.currentThread().getName());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;
    }


    @RequestMapping("/a")
    public String a() {
        System.out.println("a方法进来----");
        System.out.println("当前线程：" + Thread.currentThread().getName());
        System.out.println("a方法结束----");
        AopController o = (AopController) AopContext.currentProxy();
        o.b();
        return "a";

    }

    @Async
    public void b() {
        System.out.println("b方法进来----");
        System.out.println("当前线程：" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("b方法结束----");
    }
}
