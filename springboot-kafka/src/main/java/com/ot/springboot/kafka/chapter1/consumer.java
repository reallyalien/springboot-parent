package com.ot.springboot.kafka.chapter1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class consumer {

    //kafka集群地址
    private static final String brokerList="localhost:9092";
    //topic
    private static final String topicName="test1";
    private static final String groupId="group.demo";

    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置kay的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //设置value的反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //设置集群地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置消费组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        //创建消费者
        KafkaConsumer<String,String> consumer=new KafkaConsumer<>(properties);
        consumer.assign(Arrays.asList(new TopicPartition(topicName,0)));
        //ConsumerSyncCommit.subscribe(Collections.singleton(topicName));
        //也可以设置正则表达式去匹配主题。
        //ConsumerSyncCommit.subscribe(Pattern.compile(topicName+"*"));
        //也可以指定订阅的分区,分区索引从0开始。
        consumer.assign(Arrays.asList(new TopicPartition(topicName,0)));
        while (true){
//            ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(1000));//每隔1s去拉一条消息。
//            for (ConsumerRecord<String, String> record : records) {
//                System.out.println("value:"+record.value());
//                System.out.println("offset:"+record.offset());
//            }
        }
    }

}
