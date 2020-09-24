package com.ot.springboot.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

public class MyMessageRecover implements MessageRecoverer {

    @Override
    public void recover(Message message, Throwable throwable) {

    }
}
