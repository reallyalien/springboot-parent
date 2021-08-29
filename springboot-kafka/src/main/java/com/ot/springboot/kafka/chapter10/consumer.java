package com.ot.springboot.kafka.chapter10;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * 如果一个消费者订阅了一个topic，这个topic有2个分区，则这个消费者会消费2个分区的消息，当消费位置重置之后，消息可以重复消费
 * 也就是可以拉取到offset之前的消息，存在一个问题，kafka的消息什么时候删除掉？？？
 *
 * https://blog.csdn.net/u012809308/article/details/110006925
 *
 */
public class consumer {

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
        //指定位移消费
        /**
         * seek方法只能重置消费者分配到的分区的消费位置，而分区的分配是在poll方法当中实现的，也就说在调用seek方法之前需要先调用一下poll方法
         * 等到分区分配之后才可以重置消费位置，而poll方法带有参数，可能时间过去之后分区还没有分配，次数会返回一个空集合，因此需要等分区分配
         * 完毕之后才可以进行消费位置重置
         */
        /**
         * 消费者在消费消息时会根据之前提交的消费位移offset去kafka拉取offset之后的消息进行消费。但是一些情况下消费者开始消费时会没有消
         * 费位移：
         *
         * 一个新的消费组建立的时候；
         * 消费组内的一个新的消费者订阅了一个新的主题；
         * __consumer_offsets主题中关于这个消费组的位移信息已经过期而被删除的时候；
         * 这时消费者开始消费的消费位移就由客户端参数 auto.offset.reset 来决定，各值的含义如下：
         *
         * earliest：当各分区下存在已提交的 offset 时，从提交的 offset 开始消费；无提交的 offset 时，从头开始消费；
         * latest：当各分区下存在已提交的 offset 时，从提交的 offset 开始消费；无提交的 offset 时，消费该分区下新产生的数据（默认值）；
         * none：当各分区都存在已提交的 offset 时，从 offset 后开始消费；只要有一个分区不存在已提交的offset，则直接抛出NoOffsetForPartitionException异常；
         * Kafka 提供的 auto.offset.reset 参数也只能在找不到消费位移或位移越界的情况下粗粒度地从开头或末尾开始消费。
         *
         * 有的时候，我们需要一种更细粒度的掌控，可以让我们从指定的位移处开始拉取消息，可通过以下两种方式来实现。
         */
        Set<TopicPartition> assign=new HashSet<>();
        while (assign.isEmpty()){
            consumer.poll(Duration.ofMillis(100));
            assign=consumer.assignment();
        }
        for (TopicPartition topicPartition : assign) {
            //下面2个方法可以获取到分区的头和尾
//            System.out.println(consumer.beginningOffsets(Collections.singleton(topicPartition)));
//            System.out.println(consumer.endOffsets(Collections.singleton(topicPartition))); 这个返回的是将要插入数据的offset，不
            //是最后一条记录的offset
            consumer.seek(topicPartition,0);
        }
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);//每隔1s去拉一条消息
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("partition->" + record.partition() + "   offset->" + record.offset() + "   value->" + record.value());
            }
        }
    }

}
