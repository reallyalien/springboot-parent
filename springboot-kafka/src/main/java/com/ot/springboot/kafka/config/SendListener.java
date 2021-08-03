package com.ot.springboot.kafka.config;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

//@Component
public class SendListener implements ProducerListener {
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        //这里可以实现消息确认，需要手动添加一个监听器
        System.out.println("发送成功");
    }

    @Override
    public void onError(ProducerRecord producerRecord, Exception exception) {
        System.out.println("发送异常");
    }
}
