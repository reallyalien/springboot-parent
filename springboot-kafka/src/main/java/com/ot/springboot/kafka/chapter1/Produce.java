package com.ot.springboot.kafka.chapter1;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

@Slf4j
public class Produce {
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
            //同步发送
            for (int i = 0; i < 10; i++) {
                Future<RecordMetadata> metadataFuture = producer.send(record);
                RecordMetadata recordMetadata = metadataFuture.get();
                System.out.println("topic:"+recordMetadata.topic());
                System.out.println("partition:"+recordMetadata.partition());
                //1.每条消息都有一个当前Partition下唯一的64字节的offset，它指明了这条消息的起始位置。
                //2.在kafka服务器不关闭的情况下，每发一次数据，offset都会增加，
                //3.在服务器停止，消费者没有停止的情况下，当kafka服务器重新启动时，消费者方的offset依旧是继续增加，而不是从0开始计数
                //4.在服务器没有停止，消费者停止的情况下，当消费者继续启动时，offset依旧还是继续增加，而不是从0开始
                //5.在服务器和消费者都停止的情况下，offset还是依旧增加，我想可能是有log的问题
                // 可以理解为数组下标 offset为9，说明当前这个消息是第十个
                //偏移了9位，处在第10位
                System.out.println("offset:"+recordMetadata.offset());
            }
            //异步发送
//            producer.send(record, new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                    if (null == e){
//                        System.out.println("topic:"+recordMetadata.topic());
//                        System.out.println("partition:"+recordMetadata.partition());
//                        System.out.println("offset:"+recordMetadata.offset());
//                    }else {
//                        e.printStackTrace();
//                    }
//                }
//            });
        }catch (Exception e){
            e.printStackTrace();
        }
        producer.close();
    }

}
