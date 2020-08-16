package com.ot.springboot.hello.service;

import com.alibaba.fastjson.JSON;


public class HelloService {

    private String name;

    public String objToJson(Object obj) {
        return getName() + JSON.toJSONString(obj);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
