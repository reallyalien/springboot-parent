package com.ot.springboot.kafka.chapter3;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Slf4j
public class Produce3 {
    //kafka集群地址
    private static final String brokerList="localhost:9092";
    //topic
    private static final String topicName="test1";

    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置kay的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG,3);
        //设置value的序列化方式
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //设置集群地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //创建生成者
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String,String> record=new ProducerRecord<>(topicName,"kafka-demo","hello2");
        try {


            //异步发送
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (null == e){
                        System.out.println("topic:"+recordMetadata.topic());
                        System.out.println("partition:"+recordMetadata.partition());
                        System.out.println("offset:"+recordMetadata.offset());
                    }else {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        producer.close();
    }

}
