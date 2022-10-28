package com.ot.springboot.hello.config;

import com.ot.springboot.hello.User;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "hello")
public class HelloProperties {
    public static final String DEFAULT_NAME = "默认";

    private String name = DEFAULT_NAME;

    private Collection<String> bindingTables = new ArrayList<>();

    private List<User> userCollections = new ArrayList<User>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setBindingTables(List<String> bindingTables) {
        this.bindingTables = bindingTables;
    }

    public Collection<String> getBindingTables() {
        return this.bindingTables;
    }

    public List<User> getUserCollections() {
        return userCollections;
    }

    public void setUserCollections(List<User> userCollections) {
        this.userCollections = userCollections;
    }
}
