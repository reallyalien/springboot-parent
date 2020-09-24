package com.ot.springboot.rabbit.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

/**
 * 消息在重试还是失败以后触发这个
 */
public class MyMessageRecover implements MessageRecoverer {

    private int count = 0;

    @Override
    public void recover(Message message, Throwable throwable) {
        System.out.println("消息重发之后" + (count += 5) + "次还是失败");
        //这里做自己的逻辑
    }
}
