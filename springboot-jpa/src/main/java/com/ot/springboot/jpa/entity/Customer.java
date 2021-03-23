package com.ot.springboot.jpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * * 所有的注解都是使用 JPA 的规范提供的注解，
 * * 所以在导入注解包的时候，一定要导入 javax.persistence 下的
 */
@Getter
@Setter
@Entity //声明实体类
@Table(name = "customer") //建立实体类和表的映射关系
public class Customer {
    @Id//声明当前私有属性为主键
//    @GenericGenerator(name = "myuuid",strategy = "uuid")   hibernate声明主键，
//    @GeneratedValue(generator = "myuuid")                 但还是得需要jpa去映射主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //配置主键的生成策略
    @Column(name = "id") //指定和表中 cust_id 字段的映射关系，如果属性值和数据库表当中的字段不一样可以不写
    private Long id;

    @Column(name = "name") //指定和表中 cust_name 字段的映射关系
    private String name;

    //指定一对多关系
    @OneToMany(targetEntity = LinkMan.class, mappedBy = "customer", fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})//指明类与类的关系
    private Set<LinkMan> linkmens = new HashSet<LinkMan>(0);

}