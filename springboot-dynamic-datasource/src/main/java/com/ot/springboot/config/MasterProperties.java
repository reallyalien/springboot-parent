package com.ot.springboot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.datasource.master")
public class MasterProperties {
    private String type;
    private String driverClassName;
    private String url;
    private String username;
    private String password;


    //初始连接数
    private int initialSize;
            //最小连接池数量
    private int minIdle;
            //最大连接池数量
    private int maxActive;
            //配置获取连接等待超时的时间
    private long  maxWait;
            //配置连接超时时间
    private long connectTimeout=30000;
            //配置网络超时时间
    private long socketTimeout=60000;
            //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
    private long timeBetweenEvictionRunsMillis=60000;
            //配置一个连接在池中最小生存的时间，单位是毫秒
    private long minEvictableIdleTimeMillis=300000;
            // 配置一个连接在池中最大生存的时间，单位是毫秒
    private long maxEvictableIdleTimeMillis=900000;
            //配置检测连接是否有效
    private String validationQuery=" SELECT 1 FROM DUAL";
    private boolean testWhileIdle= true;
    private boolean testOnBorrow= false;
    private boolean testOnReturn= false;
}
