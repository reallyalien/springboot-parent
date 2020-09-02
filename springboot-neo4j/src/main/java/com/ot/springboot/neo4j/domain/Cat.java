package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

import java.io.Serializable;


public class Cat implements Serializable {


    private Long id;
    private String name;
    private String sex;

    public Cat() {
    }


    public Cat(String name, String sex) {
        this.name = name;
        this.sex = sex;
    }

    public Cat(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
