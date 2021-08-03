package com.ot.springtest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ListConfig2 {


    @Value("#{'${zk.urls:}'.empty ? null : '${zk.urls:}'.split(',')}")
    public List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
