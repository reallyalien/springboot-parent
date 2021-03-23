package com.ot.springboot.ajax;

import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.PrintStream;

@SpringBootApplication
public class AjaxApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(AjaxApplication.class);
        springApplication.addListeners();
        springApplication.addInitializers();
//        springApplication.setBanner(new ResourceBanner(new ClassPathResource("image/1.jpg")));
        springApplication.run(args);
    }


}
