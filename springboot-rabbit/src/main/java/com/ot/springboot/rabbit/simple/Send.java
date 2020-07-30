package com.ot.springboot.rabbit.simple;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbit的持久化{
 *     1.交换机的持久化
 *     2.队列的持久化：队列的持久化是通过声明队列时，将durable参数设置为true实现的。如果队列不设置持久化，
 *       那么rabbitmq服务重启之后，相关的队列元数据将会丢失，而消息是存储在队列中的，所以队列中的消息也会被丢失
 *     3.消息的持久化：队列的持久化只能保证其队列本身的元数据不会被丢失，但是不能保证消息不会被丢失。所以
 *       消息本身也需要被持久化，可以在投递消息前设置AMQP.BasicProperties的属性deliveryMode为2即可：
 * }
 */
public class Send {
    private static final String QUEUQ_NAME="test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获得链接
        Connection connection = ConnectUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        /**
         * durable：持久队列，在服务器重新启动之后依然存在
         * autoDelete：服务器不再使用时，自动删除
         */
        channel.queueDeclare(QUEUQ_NAME,true,false,false,null);
        String msg="hello simple mq";

        for (int i = 0; i < 10; i++) {
            //设置deliveryMode为2，消息可以持久化
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).build();
            channel.basicPublish("",QUEUQ_NAME,basicProperties, msg.getBytes());
            System.out.println("send msg "+msg);
        }

        channel.close();
        connection.close();
    }
}
