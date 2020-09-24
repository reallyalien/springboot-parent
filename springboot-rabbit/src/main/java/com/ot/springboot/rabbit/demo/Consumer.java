package com.ot.springboot.rabbit.demo;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 1.监听的方法内部必须使用channel进行消息确认，包括消费成功或消费失败
 * <p>
 * 2.如果不手动确认，也不抛出异常，消息不会自动重新推送（包括其他消费者），因为对于rabbitmq来说始终没有接收到消息消费是否成功的确认，
 * 并且Channel是在消费端有缓存的，没有断开连接
 * <p>
 * 3.如果rabbitmq断开，连接后会自动重新推送（不管是网络问题还是宕机）
 * <p>
 * 4.如果消费端应用重启，消息会自动重新推送
 * <p>
 * 5.如果消费端处理消息的时候宕机，消息会自动推给其他的消费者
 * <p>
 * 6.如果监听消息的方法抛出异常，消息会按照listener.retry的配置进行重发，但是重发次数完了之后还抛出异常的话，消息不会重发（也不会重发到其他消费者），只有应用重启后会重新推送。因为retry是消费端内部处理的，包括异常也是内部处理，对于rabbitmq是不知道的（此场景解决方案后面有）
 * <p>
 * 7.spring.rabbitmq.listener.retry配置的重发是在消费端应用内处理的，不是rabbitqq重发
 */
@Service
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    private int count = 0;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Constant.QUEUE),
            exchange = @Exchange(value = Constant.EXCHANGE),
            key = Constant.ROUTE_KEY)
    )
    @RabbitHandler
    public void consumer(Message message, Channel channel) throws IOException {
        MessageHeaders headers = message.getHeaders();
        long deliveryTag = (long) headers.get("amqp_deliveryTag");
        Action action = null;
        log.info("消息内容：" + message.getPayload());
        log.info("消息投递号：" + deliveryTag);
        try {
            int a = 1 / 0;
            action = Action.ACCEPT;
            Set<Map.Entry<String, Object>> entries = headers.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                //log.info("消息头信息：" + entry.getKey() + "-->" + entry.getValue());
            }
        } catch (Exception e) {
            action = Action.RETRY;
            log.info("重试第" + (++count) + "次");
            //需要判断异常类型，如果可以重试，则将异常抛出，如果不可重试（重试没有意义，则记录日志）
            throw new ArithmeticException("");
        } finally {
            if (action == Action.ACCEPT) {
                channel.basicAck(deliveryTag, false);
            } else if (action == Action.RETRY) {
                //直接将异常抛出，就可，不需要重新入队,重新入队的话，消息会一直重试，我们配置的重试次数会失效
                channel.basicNack(deliveryTag, false, true);
            } else if (action == Action.REJECT) {
                channel.basicNack(deliveryTag, false, false);
            }

        }
    }
}
