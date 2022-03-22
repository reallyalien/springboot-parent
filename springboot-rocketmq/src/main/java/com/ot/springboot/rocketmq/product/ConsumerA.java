package com.ot.springboot.rocketmq.product;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * https://www.cnblogs.com/weifeng1463/p/12889300.html
 *
 * https://blog.csdn.net/Saintmm/article/details/120829502
 */

/**
 * 同一个集群下同一个消费者组的消费者的订阅关系完全一样
 *
 * 同一个消费组，给不同的消费者设置不同的tag时，后启动的消费者会覆盖先启动的消费者设置的tag。
 *
 * tag是消息过滤的条件，经过服务端和客户端两层过滤，最后只有后启动的消费者才能收到部分消息。
 *
 * 不同的消费者启动后，依次向Broker端注册订阅关系，因为tag不一样，导致Broker端Map中同一topic的tag被覆盖。比如：消费者1订阅tag1，消费者2订阅tag2。
 * 最后Broker端的map中只保存tag2；
 * 过滤的核心是是tag，tag被更新，过滤条件被改变。服务端过滤后只返回tag2的消息；
 * 客户端接收消息后，再次过滤。先启动的消费者1订阅tagA，但是服务端返回tag2，所以消费者1收不到任何消息。消费者2能收到一半的消息（集群模式，
 * 假设消息平均分配，另外一半分给tag2）
 *
 *广播：BROADCASTING("BROADCASTING"),
 *
 *默认是集群：CLUSTERING("CLUSTERING");
 *
 *
 */
public class ConsumerA {

    public static void main(String[] args) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("DEFAULT_CONSUMER-1");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        //消费模式
//        consumer.setMessageModel(MessageModel.BROADCASTING);
        //消费位置
//        consumer.setConsumeFromWhere();
        //消息分配策略
//        consumer.setAllocateMessageQueueStrategy();
        try {
            consumer.subscribe("TopicTest","tag-a");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        //ConsumeRequest
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println(msgs.size());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        System.out.println("消费者A初始化结束...");
    }
}

