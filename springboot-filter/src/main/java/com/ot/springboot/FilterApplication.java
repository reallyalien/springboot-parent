package com.ot.springboot;

import com.ot.springboot.filter.filter.Filter1;
import com.ot.springboot.filter.filter.Filter2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class FilterApplication {

    public static void main(String[] args) {
        SpringApplication sp = new SpringApplication(FilterApplication.class);
        sp.run(args);
    }


    @Bean
    public FilterRegistrationBean filter1() {
        FilterRegistrationBean<Filter> filter1 = new FilterRegistrationBean<Filter>();
        filter1.setUrlPatterns(Arrays.asList("/*"));
        filter1.setName("filter1");
        filter1.setFilter(new Filter1());
        filter1.setOrder(10);
        return filter1;
    }

    @Bean
    public FilterRegistrationBean filter2() {
        FilterRegistrationBean<Filter> filter2 = new FilterRegistrationBean<Filter>();
        filter2.setUrlPatterns(Arrays.asList("/*"));
        filter2.setName("filter2");
        filter2.setFilter(new Filter2());
        filter2.setOrder(100);
        return filter2;
    }
}
