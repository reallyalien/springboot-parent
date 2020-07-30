package com.ot.springboot.rabbit.pubSub;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * pub/sub模式一条消息可以被多个消费者消费，对应的交换机模式为fanout，这种模式不处理路由键,只要你这个队列绑定了交换机，
 * 就可以接收到消息
 */
public class Send {

    private static String EXCHANGE_NAME="test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel(2);
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT,false);
        //发送消息
        String msg="hello pubsub";
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
        System.out.println("输出："+msg);
        //交换机需要跟队列做绑定
        channel.close();
        connection.close();
    }
}
