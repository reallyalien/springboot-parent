package com.ot.springboot.service;

import com.ot.springboot.constant.Constant;
import com.ot.springboot.domain.Mail;
import com.ot.springboot.util.MailUtil;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public class ConsumerService {
    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    private int count = 0;

    @Autowired
    private MailUtil mailUtil;


    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = Constant.QUEUE),
            exchange = @Exchange(value = Constant.EXCHANGE),
            key = Constant.ROUTE_KEY)}
    )
    @RabbitHandler
    public void consumer(Message message, Channel channel) throws IOException {
//        log.info("对象：{}" ,mail .toString());
        System.out.println("");
    }

    @RabbitHandler
    public void consumer1(@Payload String message, Channel channel) throws IOException {
        log.info("字符串：{}", message);
    }

}
