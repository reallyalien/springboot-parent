package com.ot.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.messaging.MessageChannel;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableIntegration
public class DslIntegrationConfig {

    @Bean("InputChannelDSL")
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean("OutputChannelThree")
    public MessageChannel outputChannelOne() {
        return new DirectChannel();
    }
    @ServiceActivator(inputChannel = "OutputChannelThree")
    public void receiveB(String u) {

        System.out.println("Hello User C " + u);

    }
//    @Bean
//    public HeaderValueRouter headerValueRouter() {
//        HeaderValueRouter hvr = new HeaderValueRouter("one");
//        hvr.setChannelMapping("one","outputChannel");
//        hvr.setChannelMapping("two","outputChannelTwo");
//        return hvr;
//    }
    @Bean
    public AtomicInteger integerSource() {
        return new AtomicInteger();
    }

    @Bean
    public IntegrationFlow myFlow() {
        return IntegrationFlows.from("InputChannelDSL")
                //.filter(u -> { return u instanceof User;})
//                .transform(TestTransform::toString)
                //.route(headerValueRouter())
                .channel("OutputChannelThree")
                .get();
    }
}
