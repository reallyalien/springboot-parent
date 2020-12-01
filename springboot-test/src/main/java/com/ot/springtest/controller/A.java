package com.ot.springtest.controller;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class A implements ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private A a;

    @RequestMapping("/a")
    public String a(){
        System.out.println("a方法进来----");
        System.out.println("当前线程："+Thread.currentThread().getName());
        System.out.println("a方法结束----");
        return "a";

    }
//    @Async
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
    @GetMapping("/init")
    public void init(){
        TestInitController controller = new TestInitController();
        System.out.println(controller);
    }
    @GetMapping("/select")
    public String[] select(){
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        return beanDefinitionNames;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
