package com.ot.integration.service;


import com.ot.integration.pojo.User;

public interface ReceiveService {
    void hello(User user);

    void helloMoreParam(String name, String age);

    void helloReceiveOne(User user);

    void helloReceiveTwo(User user);

    void helloReceiveThree(String name, String age);

    void helloRouterTest(User user);

    void helloRouterMap(String name, String age);

    void routerString(String str);

    void routerInteger(Integer a);

    void helloBridge(String name, String age);

    void helloTransformer(String name);

    void helloNoParam();
}
