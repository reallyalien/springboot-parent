package com.ot.springboot.rabbit.topic;

import com.jj.rabbit.util.ConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME="test_topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String routeKey="goods.del.aaa";
        for (int i = 0; i < 50; i++) {
            String msg="商品。。。。。";
            channel.basicPublish(EXCHANGE_NAME,routeKey,null,msg.getBytes());
            System.out.println("msg:"+msg);
        }

        channel.close();
        connection.close();
    }
}
