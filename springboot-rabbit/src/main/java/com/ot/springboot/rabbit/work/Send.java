package com.ot.springboot.rabbit.work;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * work模式下轮询分发，不管谁忙还是清闲，总是一人消费者一个消息
 */
public class Send {
    /**                 |----c1
     * p-----queue-----|
     *                 \-----c2
     */
    private static final String QUEUQ_NAME="test_word_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUQ_NAME,false,false,false,null);
        for (int i = 0; i < 2; i++) {
            String msg="hello work"+i;
            //队列名称充当路由键
            channel.basicPublish("",QUEUQ_NAME,null,msg.getBytes());
            System.out.println("输出msg"+msg);
            Thread.sleep(i*20);
        }
        channel.close();
        connection.close();

    }
}
