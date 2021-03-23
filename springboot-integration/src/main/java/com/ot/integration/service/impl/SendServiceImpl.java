package com.ot.integration.service.impl;

import com.ot.integration.pojo.User;
import com.ot.integration.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service
public class SendServiceImpl implements SendService {

    @Autowired
    @Qualifier("inputChannel")
    private MessageChannel testChannel;

    @Override
    public void test1() {
        System.out.println("发送消息");
        User user = new User("小明", 20);

        Message<User> message = MessageBuilder.withPayload(user).build();
        boolean send = testChannel.send(message, 1);
        System.out.println("发送成功：" + send);
    }

    @Override
    public void moreParams() {

    }

    @Override
    public void getWay() {

    }

    @Override
    public void getWayMoreParam() {

    }

    @Override
    public void testInterceptor() {

    }

    @Override
    public void bridgeSend() {

    }

    @Override
    public void filterSend(String name) {

    }

    @Override
    public void transformerSend(String name) {

    }
}
