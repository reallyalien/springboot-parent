package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
public class ExecutorChannelConfig {

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Bean
    public MessageChannel executorChannel() {
        return new ExecutorChannel(executor);
    }

    //采用单播轮询的方式，日志无顺序，是因为在不同的线程当中执行，多个任务可以跑在不同的线程去执行，执行的是同一个方法
    //下面的相当于有2个监听者，默认的轮询方式是轮询下面的方法去处理，比如2个方法的逻辑不同，这时就要使用订阅channel
    @ServiceActivator(inputChannel = "executorChannel")
    public void handlerMessage1(Message message) {
        log.info("1,message:{}", message);
    }

    @ServiceActivator(inputChannel = "executorChannel")
    public void handlerMessage2(Message message) {
        log.info("2,message:{}", message);
    }
}
