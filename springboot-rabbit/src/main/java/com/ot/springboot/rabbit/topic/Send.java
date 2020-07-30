package com.ot.springboot.rabbit.topic;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 交换机无法存储消息，所以无法像work模式一样，在启动服务发送方之后，再去启动消费方
 * 启动服务提供方，只是创建交换机，然后再去启动服务消费方，进行监听
 */
public class Send {
    private static final String EXCHANGE_NAME = "test_topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String routeKey = "topic.del.aaa.queue";
        for (int i = 0; i < 1; i++) {
            String msg = "商品:" + i;
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
            System.out.println("msg:" + msg);
        }
        channel.close();
        connection.close();
    }
}
