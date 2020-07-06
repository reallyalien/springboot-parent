package com.ot.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduleMain {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleMain.class, args);
    }
}
