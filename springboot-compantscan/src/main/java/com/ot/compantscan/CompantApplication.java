package com.ot.compantscan;

import com.ot.compantscan.service.HelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,value = {HelloService.class}))
public class CompantApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(CompantApplication.class, args);
        HelloService bean = ac.getBean(HelloService.class);
    }
}
