package com.ot.springboot.kafka.test;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Title: User
 * @Author wangtao
 * @Date 2023/7/20 10:23
 * @description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class User {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 地址
     */
    private String address;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 死亡日期
     */
    private String death;

    /**
     * 死亡日期
     */
    private String longDeath;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 体重 kg
     */
    private Float weight;

    /**
     * 身高 cm
     */
    private Double height;

    /**
     * 最近一次购物价格
     */
    private BigDecimal price;

    /**
     * 是否成年
     */
    private Boolean adult;
}
