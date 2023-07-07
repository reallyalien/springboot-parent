package com.ot.flink.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    public Long user;
    public String product;
    public int amount;
}