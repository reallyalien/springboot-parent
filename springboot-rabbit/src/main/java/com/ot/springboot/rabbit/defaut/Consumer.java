package com.ot.springboot.rabbit.defaut;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare("q1", false, false, false, null);
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String s = new String(body);
                System.out.println(s);
            }
        };
        channel.basicConsume("q1", true, consumer);
    }
}
