package com.ot.springboot.rabbit.work;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer2 {
    private static final String QUEUQ_NAME="test_word_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUQ_NAME,false,false,false,null);
        //一旦有消息就触发这个方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("{2} msg"+msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {


                }
            }
        };
        //消费者去监听消息
        boolean autoAck=true;
        channel.basicConsume(QUEUQ_NAME,autoAck,consumer);
    }
}
