package com.ot.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //注解开启STOMP协议来传输基于代理的消息，此时控制器支持使用@MessageMapping
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 相当于2个websocket的服务器端
     * @param registry
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/topicServer").setAllowedOrigins("*").withSockJS();
        registry.addEndpoint("/queueServer").setAllowedOrigins("*").withSockJS();//注册两个STOMP的endpoint，分别用于广播和点对点
    }

    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic","/user");//topic用来广播，user用来实现queue
        registry.setApplicationDestinationPrefixes("/app");//给@MessageMapping访问的时候需要加前缀
    }
}
