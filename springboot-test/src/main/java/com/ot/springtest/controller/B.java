package com.ot.springtest.controller;

import com.ot.springtest.vo.Car;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class B {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Car car;

    @GetMapping("/out")
    public String out() {
        for (int i = 0; i < 10; i++) {
            Car car = new Car();
            System.out.println("new的："+car);
        }
        System.out.println("注入的car："+car);
        return "";
    }
}
