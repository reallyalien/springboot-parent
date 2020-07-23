package com.ot.springboot.aop.service.impl;

import com.ot.springboot.aop.service.AopService;
import org.springframework.stereotype.Service;

@Service
public class AopServiceImpl implements AopService {

    @Override
    public String hello(String msg) {
        return msg + "world";
    }
}
