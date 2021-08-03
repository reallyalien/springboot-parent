package com.ot.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * springboot的SpringFactoryLoader会在spring-boot-autoconfiguration包下META-INF下的spring.factories的EnableAutoConfiguration
 *根据这个key生成对应的实现类，要想生成实现类，你得先引入对应的依赖，spring-boot-starter-web其实当中什么代码都没有,只是引入了
 * web依赖而已,task包下处理任务
 */
@SpringBootApplication
@EnableScheduling
public class ScheduleMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(ScheduleMain.class, args);
        ThreadPoolTaskExecutor executor = ac.getBean("applicationTaskExecutor", ThreadPoolTaskExecutor.class);
        ThreadPoolTaskScheduler taskScheduler = ac.getBean("taskScheduler", ThreadPoolTaskScheduler.class);
        //ThreadPoolTaskExecutor默认是8个线程数，跟机器CPU线程数一样
        String[] names = ac.getBeanNamesForType(Executor.class);
        for (String name : names) {
            System.out.println(name);
        }
    }

    /*
    在ScheduleAnnotationBeanPostProcessor的postProcessAfterInitialization方法当中处理Bean
     */
}
