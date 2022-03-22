package com.ot.springboot.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class WebServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class,args);
    }
}
