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
    private AopService service;

    /**
     * 在spring容器当中，静态属性和成员属性是一样的，因为当前类只有一个对象
     */
    private ThreadLocal<Integer> map=new ThreadLocal<>();

    @Value("${spring.datasource.activiti.driver-class-name}")
    private String driver;

    @GetMapping("/hello/{msg}")
    public String hello(@PathVariable("msg") String msg) {
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        service.hello1(msg + "1");
        service.hello2(msg + "2");
        service.hello3(msg + "3");
        service.hello4(msg + "4");
        service.hello5(msg + "5");
        service.hello6(msg + "6");
        service.hello7(msg + "7");
        service.hello8(msg + "8");
        service.hello9(msg + "9");
        return "success";
    }

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

    @GetMapping("/hello2")
    public String hello2() {
        return driver;
    }

    @GetMapping("/hello3")
    public String hello3() {
       return map.toString();
    }

    @GetMapping("/hello4")
    public String hello4() {
        return service.hello1("ss");
    }


}
