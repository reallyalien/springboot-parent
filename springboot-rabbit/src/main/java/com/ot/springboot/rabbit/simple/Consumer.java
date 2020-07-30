package com.ot.springboot.rabbit.simple;

import com.jj.rabbit.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String QUEUQ_NAME="test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        //队列声明
        channel.queueDeclare(QUEUQ_NAME,false,false,false,null);
        //事件模型，一旦有消息发送就触发
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body,"utf-8");
                System.out.println("msg："+msg);
            }
        };
        //consumer监听队列,一直阻塞
        channel.basicConsume(QUEUQ_NAME, true, consumer);
    }
}
