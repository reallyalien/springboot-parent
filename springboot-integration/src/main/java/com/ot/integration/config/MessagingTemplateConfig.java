package com.ot.integration.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;


@Configuration
@Slf4j
public class MessagingTemplateConfig {

    //只是对channel包装了一层，底层还是使用设置的channel进行发送
    @Bean
    public MessagingTemplate messageTemplate(@Autowired MessageChannel messageChannel0) {
        MessagingTemplate template = new MessagingTemplate();
        template.setDefaultChannel(messageChannel0);
        return template;
    }

    @Bean
    public MessageChannel messageChannel0() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "messageChannel0")
    public void handlerMessage(Message message) {
        log.info("1,message:{}", message);
    }
}
