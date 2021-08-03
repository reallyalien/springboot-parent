package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@Configuration
@Slf4j
public class QueueChannelConfig {

    //内部使用linkedBlockQueue 容量为Integer.MAX_VALUE
    //队列的 ServiceActivator 注解，最后会变成 PollingConsumer 类的实例，需要自己去拉数据，不像其他channel一样是直接把数据发送给它
    //debug可以在PollingConsumer的构造方法当中去观察
    //在其父类的生命周期的方法当中去创建
    @Bean
    public MessageChannel queueChannel() {
        QueueChannel queueChannel = new QueueChannel();
        return queueChannel;
    }

    @ServiceActivator(inputChannel = "queueChannel", poller = @Poller(value = "poll"))
    public void handlerMessage1(Message message) {
        log.info("1,message:{}", message);
    }

    @ServiceActivator(inputChannel = "queueChannel", poller = @Poller(value = "poll"))
    public void handlerMessage2(Message message) {
        log.info("2,message:{}", message);
    }

    @ServiceActivator(inputChannel = "queueChannel", poller = @Poller(value = "poll"))
    public void handlerMessage3(Message message) {
        log.info("3,message:{}", message);
    }
}
