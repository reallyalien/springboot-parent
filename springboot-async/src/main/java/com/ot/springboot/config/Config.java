package com.ot.springboot.config;

import com.ot.springboot.processor.MyAsyncAnnotationProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MyAsyncAnnotationProcessor myAsyncAnnotationProcessor() {
        MyAsyncAnnotationProcessor processor = new MyAsyncAnnotationProcessor();
        return processor;
    }

}
