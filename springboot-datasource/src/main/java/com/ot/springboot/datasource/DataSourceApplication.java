package com.ot.springboot.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;
import java.util.Map;

@SpringBootApplication
public class DataSourceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(DataSourceApplication.class, args);
        Map<String, DataSource> beansOfType = ac.getBeansOfType(DataSource.class);
        System.out.println();
    }
}
