package com.ot.springboot.rabbit.defaut;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.basicPublish("AMQP default","q1",null,"hello".getBytes());
    }
}
