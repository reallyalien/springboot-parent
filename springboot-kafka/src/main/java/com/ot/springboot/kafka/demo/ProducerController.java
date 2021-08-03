package com.ot.springboot.kafka.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC1 = "test1";

    private static final String TOPIC2 = "test2";

    private static final String TOPIC3 = "batch";

    @GetMapping("/produce/send1/{msg}")
    @Transactional //事务注解也会生效，yml配置是事务前缀
    public String send1(@PathVariable("msg") String msg) throws ExecutionException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send(TOPIC1, msg);
        }
        return "";
    }

    @GetMapping("/produce/send2/{msg}")
    public String send2(@PathVariable("msg") String msg) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaTemplate.send(TOPIC2, msg);
        SendResult<String, Object> result = listenableFuture.get();
        RecordMetadata metadata = result.getRecordMetadata();
        log.info("发送方--->topic:{},partition:{},offset:{},value:{}", metadata.topic(), metadata.partition(), metadata.offset(), msg);
        return "success";
    }

    @GetMapping("/produce/send3/{msg}")
    public String send3(@PathVariable("msg") String msg) throws ExecutionException, InterruptedException {
        for (int i = 0; i < 100; i++) {
            kafkaTemplate.send(TOPIC3, msg);
        }
        return "";
    }
}
