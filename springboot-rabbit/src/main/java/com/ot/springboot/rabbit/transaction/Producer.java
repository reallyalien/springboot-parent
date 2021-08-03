package com.ot.springboot.rabbit.transaction;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 事务保证确认消息到达broker、
 * 这种方式会降低消息吞吐量
 */
public class Producer {
    private static final String QUEUQ_NAME = "queue_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUQ_NAME, false, false, false, null);
        //开启事务
        try {
            channel.txSelect();
            String msg = "hello transaction";
            channel.basicPublish("", QUEUQ_NAME, null, msg.getBytes());
            channel.txCommit();
        } catch (IOException e) {
            channel.txRollback();
        } finally {
            channel.close();
            connection.close();
        }
    }
}
