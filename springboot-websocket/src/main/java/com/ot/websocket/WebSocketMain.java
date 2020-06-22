package com.ot.websocket;

import com.ot.websocket.controller.DemoController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class WebSocketMain {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(WebSocketMain.class);
        ConfigurableApplicationContext applicationContext = springApplication.run(args);
        //注入spring的上下文对象
        DemoController.setApplicationContext(applicationContext);
    }

}
