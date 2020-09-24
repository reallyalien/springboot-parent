package com.ot.springboot.springbootdemo.service.impl;

import com.ot.springboot.springbootdemo.service.AopService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AopServiceImpl implements AopService {

    /**
     * AsyncExecutionInterceptor
     */
    @Async
    @Override
    public void sleep() {
        System.out.println("sleep线程："+Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
