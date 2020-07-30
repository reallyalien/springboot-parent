package com.ot.springboot.kafka.chapter2;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class consumer2 {
    //kafka集群地址
    private static final String brokerList="localhost:9092";
    //topic
    private static final String topicName="test1";
    private static final String groupId="group.demo";

    public static void main(String[] args) {
        //创建消费者
        KafkaConsumer<String,String> consumer=new KafkaConsumer<>(initProperties());
//        ConsumerSyncCommit.subscribe(Collections.singleton(topicName));
        consumer.assign(Arrays.asList(new TopicPartition(topicName,0)));
        while (true){
            ConsumerRecords<String,String> records=consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }

    public static Properties initProperties(){
        Properties properties = new Properties();
        //设置kay的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //设置value的反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //设置集群地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置消费组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        return properties;
    }

}
