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
                    //改成手动应答之后，必须回执消息给mq服务器，否则不会接收到下一条消息
                    //delivery_tag是消息投递序号，每个channel对应一个(long类型)，从1开始
                    //到9223372036854775807范围，在手动消息确认时可以对指定delivery_tag的
                    //消息进行ack、nack、reject等操作。每次消费或者重新投递requeue后，
                    //delivery_tag都会增加，理论上该正常业务范围内，该值永远不会达到
                    //最大范围上限。可以根据每个消费者对应channel的delivery_tag消费速率计算到达最大值需要的时间。
                    //假设：每秒钟一个消费者可以消费1000w个消息(假设每个消费者一个channel)，
                    //则9223372036854775807 / (60 * 60 * 24 * 365 * 1000w) = 29247年后能达到上限数值。
                    channel.basicAck(envelope.getDeliveryTag(),false);
                    System.out.println("deliveryTag:"+envelope.getDeliveryTag());
                }
            }
        };
        //消费者去监听消息
        boolean autoAck=false;//自动应答改成false
        channel.basicConsume(QUEUQ_NAME,autoAck,consumer);
    }
}
