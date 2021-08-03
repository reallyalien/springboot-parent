package com.ot.springboot.rabbit.topic;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.*;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 符号“#”匹配路由键的一个或多个词，符号“*”匹配路由键的一个词。
 * 例如：
 * topic.#那么这个队列会会接收topic开头的消息
 * topic.*.queue那么这个队列会接收topic.aaaa.queue这样格式的消息，不接收能topic.aaaa.bbbb.queue这样格式的消息
 */
public class Consumer2 {
    private static final String EXCHANGE_NAME = "test_topic_exchange";
    private static final String QUEUE_NAME = "test_queue_topic_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "topic.#.queue");//# 匹配一个或多个，*只匹配一个

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("[2]" + msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //手动回执消息，告诉rabbitmq消息可以删除了
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }
}
