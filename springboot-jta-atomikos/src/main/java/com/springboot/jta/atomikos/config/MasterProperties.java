package com.springboot.jta.atomikos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(
        prefix = "spring.datasource.master"
)
public class MasterProperties {
    private String type;
    private String driverClassName;
    private String url;
    private String username;
    private String password;

}
