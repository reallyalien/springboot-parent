package com.ot.integration.service;

public interface SendService {
    void test1();

    void moreParams();

    void getWay();

    void getWayMoreParam();

    void testInterceptor();

    void bridgeSend();

    void filterSend(String name);

    void transformerSend(String name);
}
