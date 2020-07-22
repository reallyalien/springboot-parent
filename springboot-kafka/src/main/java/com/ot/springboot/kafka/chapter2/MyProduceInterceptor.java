package com.ot.springboot.kafka.chapter2;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MyProduceInterceptor implements ProducerInterceptor<String,Company> {

    private volatile long sendSuccess=0;
    private volatile long sendFailure=0;



    @Override
    public ProducerRecord<String, Company> onSend(ProducerRecord<String, Company> record) {
        Company company = record.value();
        company.setAddress("中国"+company.getAddress());
        return new ProducerRecord<String, Company>(record.topic(),
                record.partition(),record.timestamp(),record.key(),company,record.headers());
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            sendSuccess++;
        } else {
            sendFailure++;
        }
    }

    @Override
    public void close() {
        float successRatio = sendSuccess / (sendSuccess + sendFailure);
        System.out.println("成功率："+String.format("%f",successRatio * 100)+"%");
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
