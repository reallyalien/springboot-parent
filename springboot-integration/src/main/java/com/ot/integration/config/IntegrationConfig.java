package com.ot.integration.config;

import com.ot.integration.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.router.PayloadTypeRouter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class IntegrationConfig {

    /*
    入站通道适配器--->转换器--->路由器  转换器在传输过程中起作用，适配器路由器都是在输出或者接收端点作用
     */
//    @Transformer(inputChannel = "inputChannel", outputChannel = "outputChannel")
    public User check(User user) {
        user.setAge(user.getAge() + 100);
        return user;
    }

//    /**
//     * 输入适配器  接收方法不能直接接收
//     *
//     * @return
//     */
//    @InboundChannelAdapter(channel = "inputChannel", poller = @Poller(fixedRate = "10000"))
//    public MessageSource<User> adapter() {
//        return () -> {
//            User user = new User("js", 21);
//            return MessageBuilder.withPayload(user).build();
//        };
//    }

    /**
     * 输入适配器2 ，直接返回消息，接收方法可以直接接收
     *
     * @return
     */

//    @InboundChannelAdapter(channel = "inputChannel", poller = @Poller(fixedRate = "3000"))
    public Message<Map<String, String>> adapter1() {
//        User user = new User("ll", 23);
        Map<String,String> map=new HashMap<>();
        map.put("1","1");
        return MessageBuilder.withPayload(map).build();
    }

}
