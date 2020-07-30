package com.ot.springboot.kafka.chapter3;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;


public class ConsumerSeek {

    //kafka集群地址
    private static final String brokerList="localhost:9092";
    //topic
    private static final String topicName="test1";
    private static final String groupId="group.demo";


    public static void main(String[] args) {
        //创建消费者
        KafkaConsumer<String,String> consumer=new KafkaConsumer<>(initProperties());
        TopicPartition tp = new TopicPartition(topicName, 0);//这里指定了哪个分区
        //subscribe哪个topic的哪个partitions
        consumer.assign(Arrays.asList(tp));
        //timeout参数设置，太小的话会使分区分配失败，太长的话导致不必要的等待。
//        consumer.poll(Duration.ofMillis(2000));
        //获取消费者分配的分区
        Set<TopicPartition> topicPartitions = consumer.assignment();
        System.out.println(topicPartitions);
        for (TopicPartition topicPartition : topicPartitions) {
            consumer.seek(topicPartition,0);
        }
        while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : consumerRecords) {
                System.out.println(record.value());
            }
        }
    }

    public static Properties initProperties(){
        Properties properties = new Properties();
        //设置kay的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //设置value的反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        //设置集群地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置消费组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        //自动提交,false需要手动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,false);
        return properties;
    }
}
