package com.ot.springboot.service;

import com.ot.springboot.anno.MyAsync;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl implements AsyncService {

    @MyAsync
    @Override
    public String async() {
        return Thread.currentThread().getName();
    }

    @Override
    public void sync() {
        System.out.println(Thread.currentThread());
    }
}
