package com.ot.springboot.kafka.chapter1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

/**
 * 两个消费者属于同一个消费者组，同时订阅同一个分区，这就相当于一个分区有2个消费者，这样的话会同时拉取消息，也就是消息
 * 会重复消费
 */
public class consumer2 {

    //kafka集群地址
    private static final String brokerList = "192.168.140.128:9092";
    //topic
    private static final String topicName = "test-kafka-1";
    private static final String groupId = "group1";

    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置kay的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //设置value的反序列化方式
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //设置集群地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置消费组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
//        consumer.assign(Arrays.asList(new TopicPartition(topicName, 0)));
        //ConsumerSyncCommit.subscribe(Collections.singleton(topicName));
        //也可以设置正则表达式去匹配主题。
        //ConsumerSyncCommit.subscribe(Pattern.compile(topicName+"*"));
        //也可以指定订阅的分区,分区索引从0开始。
        consumer.assign(Arrays.asList(new TopicPartition(topicName, 0)));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);//每隔1s去拉一条消息。
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("partition->" + record.partition() + "   offset->" + record.offset() + "   value->" + record.value());
            }
        }
    }

}
