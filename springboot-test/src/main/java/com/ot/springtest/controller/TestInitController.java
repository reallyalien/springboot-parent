package com.ot.springtest.controller;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Component
public class TestInitController implements InitializingBean, DisposableBean {


    private String startStatement;
    private String returnStatement;
    private String matchStatement;
    private String whereStatement;
    private String orderByStatement;
    @PostConstruct
    public void init() {
        System.out.println("init被调用");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(StringUtils.hasText(startStatement), "A START statement is required");
        Assert.state(StringUtils.hasText(returnStatement), "A RETURN statement is required");
        Assert.state(StringUtils.hasText(orderByStatement), "A ORDER BY statement is required");
        System.out.println("afterPropertiesSet被调用");
    }

    @PreDestroy
    public void destroy2() {
        System.out.println("destroy-PreDestroy被调用");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("destroy被调用");
    }
}
