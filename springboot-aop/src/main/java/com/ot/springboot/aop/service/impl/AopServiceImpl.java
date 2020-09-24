package com.ot.springboot.aop.service.impl;

import com.ot.springboot.aop.service.AopService;
import org.springframework.stereotype.Service;

@Service
public class AopServiceImpl implements AopService {

    @Override
    public String hello1(String msg) {
        return msg + "1world";
    }
    @Override
    public String hello2(String msg) {
//        int a=1/0;
        return msg + "2world";
    }
    @Override
    public String hello3(String msg) {
//        int a=1/0;
        return msg + "3world";
    }
    @Override
    public String hello4(String msg) {
//        int a=1/0;
        return msg + "4world";
    }
    @Override
    public String hello5(String msg) {
//        int a=1/0;
        return msg + "5world";
    }
    @Override
    public String hello6(String msg) {
//        int a=1/0;
        return msg + "6world";
    }
    @Override
    public String hello7(String msg) {
//        int a=1/0;
        return msg + "7world";
    }
    @Override
    public String hello8(String msg) {
//        int a=1/0;
        return msg + "8world";
    }
    @Override
    public String hello9(String msg) {
//        int a=1/0;
        return msg + "9world";
    }
}
