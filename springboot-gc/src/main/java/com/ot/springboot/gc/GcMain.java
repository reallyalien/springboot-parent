package com.ot.springboot.gc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = TaskSchedulingAutoConfiguration.class)
@EnableScheduling
public class GcMain {

    public static void main(String[] args) {
        SpringApplication.run(GcMain.class, args);
    }
}
