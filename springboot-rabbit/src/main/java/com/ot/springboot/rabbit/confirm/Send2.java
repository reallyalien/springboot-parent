package com.ot.springboot.rabbit.confirm;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 每发送一条就调用waitForConfirm,发送一批消息之后再进行消息确认。
 */
public class Send2 {
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
        //设置交换机不能设置为null，设置成null会使用默认的交换机。
        for (int i = 0; i < 10; i++) {
            channel.basicPublish("",QUEUQ_NAME,null,msg.getBytes());
        }
        if (!channel.waitForConfirms()) {
            System.out.println("message send fail");
        }else {
            System.out.println("message send ok");
        }
        channel.close();
        connection.close();
    }
}
