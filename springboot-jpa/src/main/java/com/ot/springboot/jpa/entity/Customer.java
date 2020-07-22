package com.ot.springboot.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * * 所有的注解都是使用 JPA 的规范提供的注解，
 * * 所以在导入注解包的时候，一定要导入 javax.persistence 下的
 */

@Entity //声明实体类
@Table(name = "cst_customer") //建立实体类和表的映射关系
public class Customer {
    @Id//声明当前私有属性为主键
//    @GenericGenerator(name = "myuuid",strategy = "uuid")   hibernate声明主键，
//    @GeneratedValue(generator = "myuuid")                 但还是得需要jpa去映射主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //配置主键的生成策略
    @Column(name = "cust_id") //指定和表中 cust_id 字段的映射关系，如果属性值和数据库表当中的字段不一样可以不写
    private Long custId;

    @Column(name = "cust_name") //指定和表中 cust_name 字段的映射关系
    private String custName;

    @Column(name = "cust_source")//指定和表中 cust_source 字段的映射关系
    private String custSource;

    @Column(name = "cust_industry")//指定和表中 cust_industry 字段的映射关系
    private String custIndustry;

    @Column(name = "cust_level")//指定和表中 cust_level 字段的映射关系
    private String custLevel;

    @Column(name = "cust_address")//指定和表中 cust_address 字段的映射关系
    private String custAddress;

    @Column(name = "cust_phone")//指定和表中 cust_phone 字段的映射关系
    private String custPhone;

    @Column(name = "create_time")
    private Date createTime;
    //指定一对多关系
    @OneToMany(targetEntity = LinkMan.class,mappedBy = "customer",fetch = FetchType.EAGER)//指明类与类的关系
    private Set<LinkMan> linkmens=new HashSet<LinkMan>(0);

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSource() {
        return custSource;
    }

    public void setCustSource(String custSource) {
        this.custSource = custSource;
    }

    public String getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(String custIndustry) {
        this.custIndustry = custIndustry;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Set<LinkMan> getLinkmens() {
        return linkmens;
    }

    public void setLinkmens(Set<LinkMan> linkmens) {
        this.linkmens = linkmens;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custSource='" + custSource + '\'' +
                ", custIndustry='" + custIndustry + '\'' +
                ", custLevel='" + custLevel + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", createTime=" + createTime +
                ", linkmens=" + linkmens +
                '}';
    }
}