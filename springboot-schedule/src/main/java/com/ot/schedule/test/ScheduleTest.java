package com.ot.schedule.test;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
//@EnableAsync
public class ScheduleTest {

    private static final String CRON = "* * * * * ?";//每秒执行一次

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //    @Scheduled(cron = CRON)
//    @Scheduled(fixedDelay = 5 * 1000)
    public void test() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        Date now = new Date();
        String s = simpleDateFormat.format(now);
        System.out.println("test执行的线程------>：" + Thread.currentThread() + "&&&&&&&时间--->" + s);
//        TimeUnit.SECONDS.sleep(3);
//        System.out.println("执行的线程<------："+Thread.currentThread());
//        System.out.println("#####<<<"+ LocalDateTime.now());
    }

    @Scheduled(cron = CRON)
    public void test1() throws InterruptedException {
        try {
            int a=1/0;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        Date now = new Date();
        String s = simpleDateFormat.format(now);
        System.out.println("test1执行的线程------>：" + Thread.currentThread() + "&&&&&&&时间--->" + s);
    }

}
