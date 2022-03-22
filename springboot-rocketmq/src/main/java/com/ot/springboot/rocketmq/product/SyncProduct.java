package com.ot.springboot.rocketmq.product;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 同步发送
 */
public class SyncProduct {

    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("DEFAULT_PRODUCT-1");
        producer.setNamesrvAddr("127.0.0.1:9876");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 40; i++) {
            Message messageA = new Message("TopicTest", "tag-a", "hello".getBytes());
            Message messageB = new Message("TopicTest", "tag-b", "hello".getBytes());
            SendResult result = null;
            try {
                result = producer.send(messageA);
                result = producer.send(messageB);
            } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }
}
