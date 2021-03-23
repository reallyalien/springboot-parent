package com.ot.integration.service.impl;

import com.ot.integration.pojo.User;
import com.ot.integration.service.ReceiveService;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ReceiveServiceImpl implements ReceiveService {

//    @ServiceActivator(inputChannel = "inputChannel")
    @Override
    public void hello(User user) {
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("接收到消息：" + user);
    }

    @Override
    public void helloMoreParam(String name, String age) {

    }

    @Override
    public void helloReceiveOne(User user) {

    }

    @Override
    public void helloReceiveTwo(User user) {

    }

    @Override
    public void helloReceiveThree(String name, String age) {

    }

    @Override
    public void helloRouterTest(User user) {

    }

    @Override
    public void helloRouterMap(String name, String age) {

    }

    @Override
    public void routerString(String str) {

    }

    @Override
    public void routerInteger(Integer a) {

    }

    @Override
    public void helloBridge(String name, String age) {

    }

    @Override
    public void helloTransformer(String name) {

    }

    @Override
    public void helloNoParam() {

    }
}
