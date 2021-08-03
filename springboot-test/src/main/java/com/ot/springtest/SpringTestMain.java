package com.ot.springtest;

import com.ot.springtest.config.EnableEcho;
import com.ot.springtest.config.ListConfig;
import com.ot.springtest.config.ListConfig2;
import org.apache.catalina.core.StandardWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Map;

@SpringBootApplication
//@EnableAsync
//@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@EnableEcho(packages = {"com.ot.springtest.dto", "com.ot.springtest.vo"})
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
public class SpringTestMain {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SpringTestMain.class);
        Map<String, ListConfig2> beansOfType = ac.getBeansOfType(ListConfig2.class);
    }


}
