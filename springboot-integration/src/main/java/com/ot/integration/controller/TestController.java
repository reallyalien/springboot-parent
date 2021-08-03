package com.ot.integration.controller;

import com.ot.integration.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    @Qualifier("directChannel")
    private MessageChannel directChannel;

    @Autowired
    @Qualifier("publicScribe")
    private MessageChannel publicScribe;

    @Autowired
    @Qualifier("executorChannel")
    private MessageChannel executorChannel;

    @Autowired
    @Qualifier("queueChannel")
    private MessageChannel queueChannel;

    @Autowired
    @Qualifier("routerChannel")
    private MessageChannel routerChannel;

    @Autowired
    private GatewayService gatewayService;

    @Autowired
    @Qualifier("input")
    private MessageChannel input;

    @Autowired
    private MessagingTemplate messageTemplate;

    @GetMapping("/direct")
    public void direct() {
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload("direct").build();
            directChannel.send(message);
        }
    }

    @GetMapping("/publicScribe")
    public void publicScribe() {
        Message<String> message = MessageBuilder.withPayload("publicScribe").build();
        publicScribe.send(message);
    }

    @GetMapping("/executor")
    public void executor() {
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload("executor").build();
            executorChannel.send(message);
        }
    }

    @GetMapping("/queue")
    public void queue() {
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload("executor").build();
            //没有设置超时时间，默认放入阻塞队列当中，然后就不管了,，有另外的线程去拿数据
            queueChannel.send(message);
        }
    }

    @GetMapping("/routerString")
    public void routerString() {
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload("router").build();
            routerChannel.send(message);
        }
    }

    @GetMapping("/routerInt")
    public void routerInt() {
        for (int i = 0; i < 10; i++) {
            Message<Integer> message = MessageBuilder.withPayload(1).build();
            routerChannel.send(message);
        }
    }

    @GetMapping("/gateway")
    public void gateway() {
        gatewayService.send("gateway");
    }

    @GetMapping("/input")
    public void input() {
        Message<String> message = MessageBuilder.withPayload("哈哈哈").build();
        input.send(message);
    }

    @GetMapping("/messageTemplate")
    public void messageTemplate() {
        Message<String> message = MessageBuilder.withPayload("哈哈哈").build();
        messageTemplate.send(message);
    }
}
