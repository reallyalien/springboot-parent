package com.ot.springboot.aop;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AOPMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(AOPMain.class, args);
        System.out.println(ac);
    }
}
