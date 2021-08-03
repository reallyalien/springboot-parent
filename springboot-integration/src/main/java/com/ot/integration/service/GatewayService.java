package com.ot.integration.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface GatewayService {

    /**
     * 屏蔽调用细节
     * @param msg
     */
    @Gateway(requestChannel = "gatewayChannel")
    public void send(String msg);
}
