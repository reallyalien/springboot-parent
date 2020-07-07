package com.ot.schedule.test;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
//@EnableAsync
public class ScheduleTest {

    private static final String CRON="* * * * * ?";//每秒执行一次

    @Scheduled(cron = CRON)
    @Async
    public void test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("执行的线程："+Thread.currentThread().getName());
        System.out.println("#####>>>"+ LocalDateTime.now());
    }

}
