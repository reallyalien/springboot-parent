package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
@Slf4j
public class RouterConfig {

    @Bean
    public MessageChannel routerChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel routerChannelTo1() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel routerChannelTo2() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "routerChannelTo1")
    public void handlerMessage1(Message message) {
        log.info("1,message;{}", message);
    }

    @ServiceActivator(inputChannel = "routerChannelTo2")
    public void handlerMessage2(Message message) {
        log.info("2,message;{}", message);
    }


    @Router(inputChannel = "routerChannel") //ServiceActivator也可以
    @Bean
    public PayloadTypeRouter router() {
        //还有其他的路由策略，相当于转发
        PayloadTypeRouter router = new PayloadTypeRouter();
        //根据负载类型去判断发送到哪一个channel
        router.setChannelMapping(String.class.getName(), "routerChannelTo1");
        router.setChannelMapping(Integer.class.getName(), "routerChannelTo2");
        return router;
    }
}
