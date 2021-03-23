package com.ot.springboot.aop.controller;

import com.ot.springboot.aop.service.AopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/aop")
public class AopController {

    @Autowired
    private AopService aopService;

    /**
     * 在spring容器当中，静态属性和成员属性是一样的，因为当前类只有一个对象
     */
    private ThreadLocal<Integer> map = new ThreadLocal<>();

//    @Value("${spring.datasource.activiti.driver-class-name}")
//    private String driver;


    @GetMapping("/hello1")
    public String hello1() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + "\t当前开始时间：" + new Date().toLocaleString());
        long start = System.currentTimeMillis();
        Thread.sleep(10 * 1000);
        long end = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "\t" + this + "耗时：" + (end - start));
        System.out.println(Thread.currentThread().getName() + "\t当前结束时间：" + new Date().toLocaleString());
        return "hello1";
    }

//    @GetMapping("/hello2")
////    public String hello2() {
////        return driver;
////    }

    @GetMapping("/hello3")
    public String hello3() {
        return map.toString();
    }


    @GetMapping("/hello4/{msg}")
    public String hello4(@PathVariable("msg") String msg) {
        String s = aopService.hello1(msg);
        return s;
    }

}
