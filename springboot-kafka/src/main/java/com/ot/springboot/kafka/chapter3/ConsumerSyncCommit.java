package com.ot.springboot.kafka.chapter3;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.PrintStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ConsumerSyncCommit {
    //kafka集群地址
    private static final String brokerList = "localhost:9092";
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
        //自动提交,false需要手动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        //消费者监听开启
        consumer.subscribe(Collections.singleton(topicName));
//        ConsumerSyncCommit.subscribe(Collections.singleton(topicName));
        //也可以设置正则表达式去匹配主题。
        //ConsumerSyncCommit.subscribe(Pattern.compile(topicName+"*"));
        //也可以指定订阅的分区,分区索引从0开始。
//        TopicPartition tp = new TopicPartition(topicName);
//        consumer.assign(Arrays.asList(tp));
        long lastConsumerOffset = -1;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
//            if (records.isEmpty()) break;
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("value:" + record.value());
                System.out.println("offset:" + record.offset());
            }
//            List<ConsumerRecord<String, String>> partitionRecord = records.records(tp);
//            lastConsumerOffset = partitionRecord.get(partitionRecord.size() - 1).offset();
            //手动同步提交，手动提交有一个缺点，就是当发起调用时应用会阻塞，当然我们可以减少手动提交的频率，但这个会增加消息重复
            //的概率（和自动提交一样），另外一个解决办法就是异步提交
//            consumer.commitSync();
        }
//        System.out.println("consumed offset:" + lastCosumerOffset);
//        OffsetAndMetadata offsetAndMetadata = consumer.committed(tp);
//        System.out.println("commited offset:" + offsetAndMetadata.offset());
//        long position = consumer.position(tp);
//        System.out.println("the offset of the next record is:" + position);
    }

}
