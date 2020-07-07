package com.ot.springbatch.pojo;

import java.io.Serializable;

public class Person implements Serializable {

    private Integer id;
    private String name;
    private Integer age;
    private String nation;
    private String address;

    public Person() {

    }

    public Person(Integer id, String name, Integer age, String nation, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nation = nation;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", nation='" + nation + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
