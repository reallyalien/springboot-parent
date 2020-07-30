package com.ot.springboot.rabbit.confirm;

import com.ot.springboot.rabbit.util.ConnectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 * 异步confirm编程模型
 */
public class Send3 {
    private static final String QUEUQ_NAME = "test_confirm_1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获得链接
        Connection connection = ConnectUtil.getConnection();
        //创建通道
        Channel channel = connection.createChannel();
        //创建队列声明
        channel.queueDeclare(QUEUQ_NAME, false, false, false, null);
        //生产者调用，将channel设置成confirm模式
        channel.confirmSelect();
        /*
        我们需要自己为每一个channel维护一个unconfirm的消息序列号，每publish一条数据，集合中元素+1，
        每回调一次handleAck方法，unconfirm集合删掉相应的一条（multiple=false）或多条（multiple=true）
        记录，从程序运行效率来看，这个unconfirm集合最好采用有序集合SortedSet存储结构。
         */
        //声明一个sortedSet,存放未确认的消息标识，线程安全的
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        //channel监听回调，此方法为异步回调
        channel.addConfirmListener(new ConfirmListener() {
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("handleNack---multiple true");
                    //headSet返回从开始到指定位置元素的集合。
                    //sortedSet.add(1L);
                    //sortedSet.add(2L);
                    //sortedSet.add(3L);
                    //SortedSet<Long> set = sortedSet.headSet(2L)   set=>1
                    SortedSet<Long> set = confirmSet.headSet(deliveryTag + 1);
                    System.out.println(set);
                    set.clear();
                } else {
                    System.out.println("handleNack---multiple false");
                    System.out.println("deliveryTag：" + deliveryTag);
                    confirmSet.remove(deliveryTag);
                }
            }

            /**
             * 可以一次性确认多条，也可以一次性确认一条
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if (multiple) {
                    System.out.println("handleAck---multiple true");
                    SortedSet<Long> set = confirmSet.headSet(deliveryTag + 1);
                    System.out.println(set);
                    set.clear();
                } else {
                    System.out.println("handleAck---multiple false");
                    confirmSet.remove(deliveryTag);
                    System.out.println("deliveryTag：" + deliveryTag);
                }
            }
        });
        String msg = "hello confirm1";
        //设置交换机不能设置为null，设置成null会使用默认的交换机。
        int i = 0;
        while (i < 100) {
            //发消息很快，异步去等待rabbit的broker去回传确认消息很慢，
            long seqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUQ_NAME, null, msg.getBytes());
            confirmSet.add(seqNo);
            System.out.println("seqNo:" + seqNo);
            i++;
        }
        Thread.sleep(3000);
        System.out.println("===========================================================");
        System.out.println(confirmSet.size());//这个数在消息都确认之后会等于0
    }
}
