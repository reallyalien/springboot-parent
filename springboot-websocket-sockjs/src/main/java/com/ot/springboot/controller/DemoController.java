package com.ot.springboot.controller;

import com.ot.springboot.msg.ReceiveMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.concurrent.TimeUnit;

@Controller
public class DemoController {
    public static final Logger log= LoggerFactory.getLogger(DemoController.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/topic")
    public void topic(ReceiveMessage msg) throws InterruptedException {
        log.info("msg,{}",msg);
        for (int i = 0; i < 5; i++) {
            //广播使用covertAndSend方法，第一个参数是目的地，和js中订阅的目的地要一致
            messagingTemplate.convertAndSend("/topic/getResponse", msg.getMsg());
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @MessageMapping("/queue")
    public void queue(ReceiveMessage msg) throws InterruptedException {
        log.info("msg:{}",msg);
        for (int i = 0; i < 5; i++) {
            //广播使用covertAndSend方法，第一个是用户id
            //此时js当中的订阅地址为"/user/" + 用户Id + "/message",其中"/user"是固定的*/
            messagingTemplate.convertAndSendToUser(msg.getUser(),"/message",msg.getMsg());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
