package com.ot.springboot.kafka.chapter3;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Consumer3_1 {
    //kafka集群地址
    private static final String brokerList = "localhost:9092";
    //topic
    private static final String topicName = "test-kafka-1";
    private static final String groupId = "group.demo1";

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
        //自动提交,false需要手动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        //ConsumerSyncCommit.subscribe(Collections.singleton(topicName));
        //也可以设置正则表达式去匹配主题。
        //ConsumerSyncCommit.subscribe(Pattern.compile(topicName+"*"));
        //也可以指定订阅的分区,分区索引从0开始。
        TopicPartition tp = new TopicPartition(topicName, 0);
        consumer.assign(Arrays.asList(tp));
        long lastConsumerOffset = -1;
        while (true) {
            //poll方法会返回我们没有消费的消息，当消息从broker返回消费者时，broker并不会跟踪这些消息是否被消费者接收到，kafka让消费者自身
            //来管理消息的位移，并向消费者提供更新位移的接口，这种更新唯一的方式称为提交（commit）
            ConsumerRecords<String, String> records = consumer.poll(1000);
            if (records.isEmpty()) break;
            List<ConsumerRecord<String, String>> partitionRecord = records.records(tp);
            lastConsumerOffset = partitionRecord.get(partitionRecord.size() - 1).offset();
            consumer.commitSync();
        }
        System.out.println("consumed offset:" + lastConsumerOffset);
        OffsetAndMetadata offsetAndMetadata = consumer.committed(tp);
        System.out.println("commited offset:" + offsetAndMetadata.offset());
        long position = consumer.position(tp);
        System.out.println("the offset of the next record is:" + position);
    }

}
