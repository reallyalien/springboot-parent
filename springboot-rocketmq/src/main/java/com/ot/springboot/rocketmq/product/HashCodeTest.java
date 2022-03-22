package com.ot.springboot.rocketmq.product;

public class HashCodeTest {

    public static void main(String[] args) {
        System.out.println("Aa".hashCode());
        System.out.println("BB".hashCode());
        System.out.println("ABCDEa123abc".hashCode());
        System.out.println("ABCDFB123abc".hashCode());
    }
}
