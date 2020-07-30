package com.ot.springboot.rabbit.confirm;

import com.jj.rabbit.util.ConnectUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 每发送一条就调用waitForConfirm
 */
public class Send1 {
    private static final String QUEUQ_NAME="test_confirm_1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获得链接
        Connection connection = ConnectUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建队列声明
        channel.queueDeclare(QUEUQ_NAME,false,false,false,null);
        //生产者调用，将channel设置成confirm模式
        channel.confirmSelect();
        String msg="hello confirm1";
        //设置交换机不能设置为null，设置成null会使用默认的交换机,设置为空，表示不需要交换机，
        channel.basicPublish("",QUEUQ_NAME,null,msg.getBytes());
        if (!channel.waitForConfirms()) {
            System.out.println("message send fail");
        }else {
            System.out.println("message send ok");
        }
        channel.close();
        connection.close();
    }
}
