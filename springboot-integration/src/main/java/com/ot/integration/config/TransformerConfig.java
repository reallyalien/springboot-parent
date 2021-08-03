package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@Configuration
@Slf4j
public class TransformerConfig {

    @Bean
    public MessageChannel input() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel output() {
        return new DirectChannel();
    }

    @Transformer(inputChannel = "input", outputChannel = "output")
    public String handlerMessage(Message message) {
        Object payload = message.getPayload();
        if (payload instanceof String) {
            String str = (String) payload;
            str = "转换后：->" + str;
            return str;
        }
        return "1";
    }
    @ServiceActivator(inputChannel = "output")
    public void handlerMessage(String s){
        log.info("1,message:{}", s);
    }
}
