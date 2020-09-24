package com.ot.springboot.rabbit.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //无论消息发送如何，都强制开启调用回调函数
        rabbitTemplate.setMandatory(true);
        //确认的回调，确认消息是否达到broker服务器，其实就是是否到达交换器，如果发送时指定的交换机不存在，
        //则ack就是false，代表消息不可达
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
                log.info("ConfirmCallback:     " + "确认情况：" + ack);
                log.info("ConfirmCallback:     " + "原因：" + cause);
            }
        });
        //消息失败的回调，如果消息已经到达交换机上，但路由键匹配不到任何绑定到该交互机的队列，会触发这个回调
        //此时replyText:NO_ROUTE
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("ReturnCallback:     " + "消息：" + message);
                log.info("ReturnCallback:     " + "回应码：" + replyCode);
                log.info("ReturnCallback:     " + "回应信息：" + replyText);
                log.info("ReturnCallback:     " + "交换机：" + exchange);
                log.info("ReturnCallback:     " + "路由键：" + routingKey);
            }
        });
        return rabbitTemplate;
    }
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate){
        MyMessageRecover myMessageRecover = new MyMessageRecover();
        return myMessageRecover;
    }
}
