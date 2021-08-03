package com.ot.springboot.kafka.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConsumerService {

    //如果主题只有1个分区，增加消费者没有用，其他消费者根本分不到分区的数据
    @KafkaListener(topics = {KafkaConstant.TOPIC1, KafkaConstant.TOPIC2}, groupId = "A")
    public void listener1(ConsumerRecord<?, ?> record, Acknowledgment acknowledgment) {
        log.info("接收方1--->topic:{},offset:{},value:{}，线程：{}", record.topic(), record.offset(), record.value(), Thread.currentThread());
//        acknowledgment.nack(3000);
        //ack之后消费者就不会重新拉取，默认是同步commit,
        acknowledgment.acknowledge();
    }


    //    @KafkaListener(topics = {KafkaConstant.TOPIC1, KafkaConstant.TOPIC2}, groupId = "B")
    public void listener2(ConsumerRecord<?, ?> record) {
        log.info("接收方2--->topic:{},offset:{},value:{},线程：{}", record.topic(), record.offset(), record.value(), Thread.currentThread());
    }

    //批量消息
//    @KafkaListener(topics = {"batch"}, containerFactory = "batchContainerFactory",groupId = "A")
    public void consumerBatch(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        log.info("接收到消息数量：{}", records.size());
        //手动提交
        ack.acknowledge();
    }


    /*
    @KafkaListener 属性

id：消费者的id，当GroupId没有被配置的时候，默认id为GroupId
containerFactory：上面提到了@KafkaListener区分单数据还是多数据消费只需要配置一下注解的containerFactory属性就可以了，这里面配置的是监听容器工厂，也就是ConcurrentKafkaListenerContainerFactory，配置BeanName
topics：需要监听的Topic，可监听多个
topicPartitions：可配置更加详细的监听信息，必须监听某个Topic中的指定分区，或者从offset为200的偏移量开始监听
errorHandler：监听异常处理器，配置BeanName
groupId：消费组ID
idIsGroup：id是否为GroupId
clientIdPrefix：消费者Id前缀
beanRef：真实监听容器的BeanName，需要在 BeanName前加 “__”
     */
    /*
    消费机制的不同：
    1.rabbitmq消费完数据ack之后，就从内存或者硬盘删除，未被确认的消息重新回到队列等待消费
    2.kafka是通过消息的偏移量来确认的，第一条消息未被确认，第二条消息被确认的时候，kafka会保存第二个消息的偏移量，也就是第一条
    消息再也不会被获取到，除非使用seek手动拉取之前的消息
     */
}
