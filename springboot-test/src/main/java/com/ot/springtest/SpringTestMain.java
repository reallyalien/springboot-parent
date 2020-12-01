package com.ot.springtest;

import com.ot.springtest.config.EnableEcho;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync
//@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
//@EnableEcho(packages = {"com.ot.springtest.dto", "com.ot.springtest.vo"})
public class SpringTestMain {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestMain.class);
    }
}
