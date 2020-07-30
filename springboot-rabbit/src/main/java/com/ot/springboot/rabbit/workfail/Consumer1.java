package com.ot.springboot.rabbit.workfail;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
//公平分发模式下，必须改成手动应答模式和限制消费者每次只能拿一条数据,
public class Consumer1 {
    private static final String QUEUQ_NAME="test_word_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectUtil.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUQ_NAME,false,false,false,null);
        channel.basicQos(1);//保证一次只分发一个
        //一旦有消息就触发这个方法
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("{1} msg"+msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //回复回执消息给mq服务器
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //消费者去监听消息
        boolean autoAck=false;//自动应答改成false
        channel.basicConsume(QUEUQ_NAME,autoAck,consumer);
    }
}
