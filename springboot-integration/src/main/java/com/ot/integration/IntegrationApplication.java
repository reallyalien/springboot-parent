package com.ot.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.endpoint.PollingConsumer;
import org.springframework.integration.handler.ServiceActivatingHandler;

import java.util.Map;

@SpringBootApplication
public class IntegrationApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(IntegrationApplication.class, args);
        Map<String, PollingConsumer> type = ac.getBeansOfType(PollingConsumer.class);
    }
}
