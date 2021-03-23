package com.ot.springboot;

import com.ot.springboot.anno.MyEnableAsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@SpringBootApplication
@MyEnableAsync
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AsyncApplication.class);
        ConfigurableApplicationContext ac = springApplication.run(args);
        String[] names = ac.getBeanNamesForType(Executor.class);
        for (String name : names) {
            System.out.println(name);
        }
    }
}
