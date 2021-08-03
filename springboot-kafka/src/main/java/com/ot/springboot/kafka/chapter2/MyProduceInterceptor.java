package com.ot.springboot.kafka.chapter2;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 修改消息内内容，统计需求，过滤不符合要求的内容等等
 */
public class MyProduceInterceptor implements ProducerInterceptor<String, String> {

    private volatile long sendSuccess = 0;
    private volatile long sendFailure = 0;


    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String company = record.value();
        System.out.println("拦截器执行了");
        return record;
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
        System.out.println("成功率：" + String.format("%f", successRatio * 100) + "%");
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
