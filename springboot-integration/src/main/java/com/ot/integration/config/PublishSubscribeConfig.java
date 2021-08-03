package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
public class PublishSubscribeConfig {

    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Bean
    public MessageChannel publicScribe() {
        return new PublishSubscribeChannel();
    }

    //使用广播BroadcastingDispatcher 分发消息,如果2个方法接收相同的数据处理不一样的逻辑，此时使用PublishSubscribeChannel
    //一定要传入线程池，否则只会使用一个线程去处理channel数据
    @ServiceActivator(inputChannel = "publicScribe")
    public void handlerMessage1(Message message) {
        log.info("1,message:{}", message);
    }

    @ServiceActivator(inputChannel = "publicScribe")
    public void handlerMessage2(Message message) {
        log.info("2,message:{}", message);
    }

}
