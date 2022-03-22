package com.ot.springboot;

import com.ot.springboot.anno.MyEnableAsync;
import com.ot.springboot.service.AsyncService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;

@SpringBootApplication
@MyEnableAsync
@EnableScheduling
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AsyncApplication.class);
    }
}
