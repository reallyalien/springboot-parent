package com.ot.springboot.kafka.chapter2;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;


import java.util.Properties;
import java.util.concurrent.Future;

@Slf4j
public class Produce2 {
    //kafka集群地址
    private static final String brokerList = "localhost:9092";
    //topic
    private static final String topicName = "test-kafka-1";

    public static void main(String[] args) {
        //创建生成者，在生成producer的时候会生成sender（实现了Runnable接口）和kafkaThread,设置成守护线程
        KafkaProducer<String, String> producer = new KafkaProducer<>(initProperties());
        //创建消息主体
        Company company = Company.builder().name("kafka").address("beijing").build();
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, JSON.toJSONString(company));
        try {
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata recordMetadata = future.get();
            System.out.println("topic:" + recordMetadata.topic());
            System.out.println("partitions:" + recordMetadata.partition());
            System.out.println("offset:" + recordMetadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }

    public static Properties initProperties() {
        Properties properties = new Properties();
        //设置kay的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //设置重试次数,默认会间隔100ms来重试，可以通过修改retry.backoff.ms参数来修改
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        //自定义value的序列化方式
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //设置集群地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //采用自己自定义的分区策略
        properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DefaultPartition.class);
        //自定义拦截器
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MyProduceInterceptor.class.getName());
        //设置ack
        properties.put(ProducerConfig.ACKS_CONFIG, "0");//注意这是字符串类型
        //默认为1，集群当中只要leader节点收到消息，生产者就能收到响应，此时如果leader节点挂掉，来不及选举leader，或者leader接收到消息，来不及
        //发送到follower,leader挂掉，消息也会丢失
        //0:只发不管，这种情况吞吐量很大，即使消息丢失，发送端也无法感知
        //-1：集群当中的所有节点都收到消息生成者才确认消息成功发送。
        return properties;
    }
}
