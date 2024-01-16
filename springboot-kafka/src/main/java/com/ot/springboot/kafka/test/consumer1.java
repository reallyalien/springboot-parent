package com.ot.springboot.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.regex.Pattern;

public class consumer1 {

    //kafka集群地址
//    private static final String brokerList = "192.168.2.111:6667";
    private static final String brokerList = "192.168.197.130:9092";
    //topic
    private static final String topicName = "test_kafka_consumer";
    private static final String groupId = "groupA";

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
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        //设置自动提交，为false
//        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //也可以设置正则表达式去匹配主题。
        consumer.subscribe(Pattern.compile(topicName));
        //也可以指定订阅的分区,分区索引从0开始。
        TopicPartition topicPartition = new TopicPartition(topicName, 0);
//        consumer.assign(Arrays.asList(topicPartition));
//        consumer.seekToBeginning(Collections.singletonList(topicPartition));
//        consumer.seekToEnd(Collections.singletonList(topicPartition));
//        consumer.seek(topicPartition,10);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);//每隔1s去拉一条消息。
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("partition->" + record.partition() + "   offset->" + record.offset() + "   value->" + record.value());
            }
//            consumer.commitSync();
        }
    }

}
