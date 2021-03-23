package com.ot.springboot.aop.service.impl;

import com.ot.springboot.aop.anno.Hello;
import com.ot.springboot.aop.service.AopService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class AopServiceImpl implements AopService {

    @Override
    @Hello
    public String hello1(String msg) {
        return msg + "1world";
    }

}
