package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
@Slf4j
public class GatewayConfig {

    @Bean
    public MessageChannel gatewayChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "gatewayChannel")
    public void handlerMessage1(Message message) {
        log.info("1,message:{}", message);
    }

    @ServiceActivator(inputChannel = "gatewayChannel")
    public void handlerMessage2(Message message) {
        log.info("2,message:{}", message);
    }
}
