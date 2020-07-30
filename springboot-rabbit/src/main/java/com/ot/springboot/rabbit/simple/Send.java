package com.ot.springboot.rabbit.simple;

import com.jj.rabbit.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String QUEUQ_NAME="test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得链接
        Connection connection = ConnectUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建队列声明
        channel.queueDeclare(QUEUQ_NAME,false,false,false,null);
        String msg="hello simple mq";
        channel.basicPublish("",QUEUQ_NAME,null, msg.getBytes());
        System.out.println("send msg "+msg);
        channel.close();
        connection.close();
    }
}
