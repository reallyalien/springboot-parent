package com.ot.springboot.kafka.chapter3;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsumerAsyncCommit {
    //kafka集群地址
    private static final String brokerList="localhost:9092";
    //topic
    private static final String topicName="test1";
    private static final String groupId="group.demo";

    private static AtomicBoolean running=new AtomicBoolean(true);
    public static void main(String[] args) {
        //创建消费者
        KafkaConsumer<String,String> consumer=new KafkaConsumer<>(initProperties());
        TopicPartition tp = new TopicPartition(topicName, 0);
        consumer.assign(Arrays.asList(tp));
        try {
            while(running.get()){
                ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    //
                    System.out.println(consumerRecord.value());
                }
                //异步回调
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                        if (exception == null){
                            Set<Map.Entry<TopicPartition, OffsetAndMetadata>> entries = offsets.entrySet();
                            entries.forEach(entry-> System.out.println(entry.getKey()+"@@"+entry.getValue().metadata()));
                        }else {
                            System.out.println("fail to commit offset"+offsets+"@@"+exception);
                        }
                    }
                });
            }
        }finally {
            consumer.close();
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
