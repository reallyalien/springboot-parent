package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
@Slf4j
public class DirectChannelConfig {


    @Bean
    public MessageChannel directChannel() {
        //默认的构造方法，内部的 UnicastingDispatcher 是默认生成的，无法传入线程池，因此此directChannel只能使用
        //调用者线程处理，无法使用线程池
        DirectChannel directChannel = new DirectChannel();
        return directChannel;
    }

    // EventDrivenConsumer 在当前这个配置类当中，这个对象创建了3个，是当前的 directChannel 与serverActiveHandler做绑定操作,
    // 因此在操作directChannel的send方法的时候从handler当中选出一个进行处理
    //每一个加注解的都会转变成 ServiceActivatingHandler，在后置处理器当中去创建的
    // UnicastingDispatcher 单播,处理消息的分发
    // RoundRobinLoadBalancingStrategy 默认使用的这个策略，轮询的方式，由调用者线程处理，可以
    @ServiceActivator(inputChannel = "directChannel")
    public void handlerMessage1(Message message) {
        log.info("1,message;{}", message);
    }

    @ServiceActivator(inputChannel = "directChannel")
    public void handlerMessage2(Message message) {
        log.info("2,message;{}", message);
    }

    @ServiceActivator(inputChannel = "directChannel")
    public void handlerMessage3(Message message) {
        log.info("3,message;{}", message);
    }
}
