package com.ot.springboot.service;

import org.springframework.stereotype.Service;

import java.beans.Transient;

@Service
public class BServiceImpl implements BService{

    @Transient
    @Override
    public void test() {
        System.out.println();
    }
}
