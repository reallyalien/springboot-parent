package com.ot.springboot.gc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean
    public Executor asyncExecutorService() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        //配置核心线程数
        pool.setCorePoolSize(5);
        //配置最大线程数
        pool.setMaxPoolSize(10);
        //配置队列大小
        pool.setQueueCapacity(400);
        //配置线程池当中的名称前缀
        pool.setThreadNamePrefix("MyThread-");
        //配置拒绝策略,由调用者来处理
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        pool.initialize();
        return pool;
    }

    public static void main(String[] args) {
        System.out.println(1 << 2);
    }
}
