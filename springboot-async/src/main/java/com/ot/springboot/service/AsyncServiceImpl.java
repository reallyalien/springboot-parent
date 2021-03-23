package com.ot.springboot.service;

import com.ot.springboot.anno.MyAsync;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

    @MyAsync
    @Override
    public void async() {
        System.out.println(Thread.currentThread());
    }

    @Override
    public void sync() {
        System.out.println(Thread.currentThread());
    }
}
