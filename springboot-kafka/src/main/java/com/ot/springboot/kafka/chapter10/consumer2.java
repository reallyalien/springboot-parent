package com.ot.springboot.kafka.chapter10;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * 两个消费者属于同一个消费者组，同时订阅同一个分区，这就相当于一个分区有2个消费者，这样的话会同时拉取消息，也就是消息
 * 会重复消费
 */
public class consumer2 {

    //kafka集群地址
    private static final String brokerList = "192.168.140.128:9092";
    //topic
    private static final String topicName = "test-kafka-10";
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
        //开启订阅
        consumer.subscribe(Collections.singleton(topicName));
        Set<TopicPartition> assignment = new HashSet<>();
        while (assignment.isEmpty()) {
            consumer.poll(Duration.ofMillis(200));
            assignment = consumer.assignment();
        }
        Map<TopicPartition, Long> map = new HashMap<>();
        for (TopicPartition topicPartition : assignment) {
            map.put(topicPartition, System.currentTimeMillis() - 24 * 3600 * 1000);
        }
        //此方法会返回时间戳大于等于查询时间的第一条消息对应的offset和时间戳
        Map<TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(map);
        for (TopicPartition topicPartition : assignment) {
            OffsetAndTimestamp offsetAndTimestamp = offsets.get(topicPartition);
            //如果这个不等于null，相当于当前分区有符合条件的消息
            if (offsetAndTimestamp != null) {
                consumer.seek(topicPartition,offsetAndTimestamp.offset());
            }
        }
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);//每隔1s去拉一条消息。
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("partition->" + record.partition() + "   offset->" + record.offset() + "   value->" + record.value());
            }
        }
    }

}
