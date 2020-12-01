package com.ot.springtest.vo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Car implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public String out(){
        return applicationContext.toString();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public static void main(String[] args) {
       Object a=10;
       long b= (long) a;
        System.out.println(b);
    }
}
