package com.ot.springboot.aop;


import com.ot.springboot.aop.anno.Hello;
import com.ot.springboot.hello.config.HelloProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AOPMain {


    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(AOPMain.class, args);
        HelloProperties bean = ac.getBean(HelloProperties.class);
        System.out.println(ac);
    }


    @Autowired
    private HelloProperties helloProperties;


}
