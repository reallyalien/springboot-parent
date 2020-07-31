package com.ot.springboot.kafka.demo;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "test1";

    @GetMapping("/produce/send/{msg}")
    public String send(@PathVariable("msg") String msg) throws ExecutionException, InterruptedException {
        ListenableFuture<SendResult<String, Object>> listenableFuture = kafkaTemplate.send(TOPIC, msg);
        SendResult<String, Object> result = listenableFuture.get();
        RecordMetadata metadata = result.getRecordMetadata();
        System.out.printf("topic is %s, partition is %d, offset is %s \n", metadata.topic(), metadata.partition(), metadata.offset());
        return "success";
    }
}
