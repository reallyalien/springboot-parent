package com.ot.springboot.springproject.config;

import com.ot.springboot.springproject.bean.StudentBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public StudentBean studentBean(){
        StudentBean studentBean = new StudentBean();
        studentBean.setId(10);
        studentBean.setName("xx");
        return studentBean;
    }
}
