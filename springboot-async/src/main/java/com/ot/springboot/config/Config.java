package com.ot.springboot.config;

import com.ot.springboot.processor.MyAsyncAnnotationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class Config {

    @Bean
    public MyAsyncAnnotationProcessor myAsyncAnnotationProcessor() {
        MyAsyncAnnotationProcessor processor = new MyAsyncAnnotationProcessor();
        return processor;
    }
    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Scheduled(cron = "* * * * * ?")
    public void q(){
        for (int i = 0; i < 100; i++) {
            executor.submit(()->{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread());
            });
        }
    }

}
