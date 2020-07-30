package com.ot.springboot.rabbit.route;

import com.jj.rabbit.util.ConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
    private static final String EXCHANGE_NAME="test_exchange_direct";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String msg="hello direct";
        String routekey="error";
        channel.basicPublish(EXCHANGE_NAME,routekey,null,msg.getBytes());
        System.out.println("send:"+msg);
        channel.close();
        connection.close();
    }
}
