package com.ot.springboot.rabbit.workfail;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 公平分发
 */
public class Send {
    /**
     * |----c1
     * p-----queue-----|
     * \-----c2
     */
    private static final String QUEUQ_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        //第二个参数就是是否做持久化，不能以不同的参数去定义一个已经存在的队列。
        channel.queueDeclare(QUEUQ_NAME, false, false, false, null);
        for (int i = 1; i <= 20; i++) {
            String msg = "hello work" + i;
            channel.basicPublish("", QUEUQ_NAME, null, msg.getBytes());
            System.out.println("输出msg" + msg);
        }
        channel.close();
        connection.close();
    }
}
