package com.ot.springboot.jpa.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Table(name = "linkman")
public class LinkMan {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "cust_id",referencedColumnName = "id")//name 当前表的外键 reference：参照的主表主键的列名
    private Customer customer;

}
