package com.ot.springboot.kafka.chapter10;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

/**
 * 一个topic2个分区，2个消费者不指定分区进行订阅，每个消费者会拉取到不同分区的消息，也就是不能指定分区，就算指定
 * 分区也得是不同的分区，否则消费会重复消费，
 */
@Slf4j
public class Produce {
    //kafka集群地址
    private static final String brokerList = "192.168.140.128:9092";
    //topic
    private static final String topicName = "test-kafka-10";

    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置kay的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        //设置value的序列化方式
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置集群地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //创建生成者
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        try {
            //同步发送
            for (int i = 0; i < 10; i++) {
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, "hello" + i);
                Future<RecordMetadata> metadataFuture = producer.send(record);
                RecordMetadata recordMetadata = metadataFuture.get();
                System.out.println("partition:" + recordMetadata.partition() + "    offset:" + recordMetadata.offset());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }

}
