package com.ot.springboot.controller;

import com.ot.springboot.constant.Constant;
import com.ot.springboot.domain.Mail;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ProviderController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/send")
    public void send() {
        Mail mail = new Mail();
        mail.setContent("测试");
        mail.setTitle("测试标题");
        mail.setTo("1754241423@qq.com");
        String msgId = UUID.randomUUID().toString();
        mail.setMsgId(msgId);
        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(Constant.EXCHANGE, Constant.ROUTE_KEY, mail, correlationData);
    }
    @GetMapping("/send1")
    public void send1() {
        String msgId = UUID.randomUUID().toString();
        CorrelationData correlationData = new CorrelationData(msgId);
        rabbitTemplate.convertAndSend(Constant.EXCHANGE, Constant.ROUTE_KEY, "世界你好", correlationData);
    }
}
