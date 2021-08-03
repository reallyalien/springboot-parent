package com.ot.springboot.rabbit.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectUtil {

    //获取链接
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置服务地址
        connectionFactory.setHost("127.0.0.1");
        //设置端口
        connectionFactory.setPort(5672);
        //设置哪个数据库
        connectionFactory.setVirtualHost("/");
        //设置用户名和密码，没有设置默认超级管理员guest/guest
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        return connectionFactory.newConnection();
    }
}
