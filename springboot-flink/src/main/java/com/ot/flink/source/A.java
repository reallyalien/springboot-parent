package com.ot.flink.source;

/**
 * @Title: A
 * @Author wangtao
 * @Date 2023/8/15 16:48
 * @description:
 */
public class A {

    public static void main(String[] args) {
        long a = 365 * 24 * 60 * 60;
        long b = 10000000;
        System.out.println(Long.MAX_VALUE / b / a);
        System.out.println(Integer.MAX_VALUE / b / a);
    }
}
