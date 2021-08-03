package com.ot.springboot.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.listener.ContainerProperties;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class KafkaMain {

    public static void main(String[] args) {
        SpringApplication.run(KafkaMain.class, args);
    }

    /**
     * The offset commit behavior enumeration.
     */
    public enum AckMode {
        //每条消息被消费完成之后自动提交
        RECORD,

        //每条消息被消费完成之后，再下一次消息到来之前自动提交，（默认）
        BATCH,

        //到达一定时间间隔之后，自动提交，并不是立马提交，如果此时正在消费某条消息，需要等待这些消息消费完才能提交
        TIME,

        //消息到达一定数量之后提交，自动提交，并不是立马提交，如果此时正在消费某条消息，需要等待这些消息消费完才能提交
        COUNT,

        //上面2个的结合体
        COUNT_TIME,

        //手动提交，调用前，先标记消费进度，然后再提交
        MANUAL,

        //调用时，立即提交消费进度
        MANUAL_IMMEDIATE,

    }
    // ConcurrentMessageListenerContainer 这是springboot帮我们创建的消息监听容器的类，会在doStart方法当中创建与既定配置并发度的
    // KafkaMessageListenerContainer 创建完成之后再调用其doStart方法


    //一个消费者对应一个分区，如果没有发生再平衡，多个消费者对应同一topic的多个分区（启动多个线程意味着多个消费者），不会出现重复消费和消息丢失的现象

    // PartitionAssignor 分区策略，kafka本身提供了三种，默认的是 range,可以通过修改消费方参数来修改
}
