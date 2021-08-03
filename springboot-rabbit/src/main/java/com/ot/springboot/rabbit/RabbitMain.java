package com.ot.springboot.rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitMain {

    public static void main(String[] args) {
        SpringApplication.run(RabbitMain.class, args);
    }


    //rabbit启动成功后无法访问，在sbin目录下执行，rabbitmq-plugins enable rabbitmq_management

    //启动成功显示,如下
    /*

  ##  ##
  ##  ##      RabbitMQ 3.7.7. Copyright (C) 2007-2018 Pivotal Software, Inc.
  ##########  Licensed under the MPL.  See http://www.rabbitmq.com/
  ######  ##
  ##########  Logs: C:/Users/Monsters/AppData/Roaming/RabbitMQ/log/RABBIT~1.LOG
                    C:/Users/Monsters/AppData/Roaming/RabbitMQ/log/rabbit@DESKTOP-QJB0AF6_upgrade.log

              Starting broker...
 completed with 3 plugins.

     */

    //默认登录 guest/guest
}
