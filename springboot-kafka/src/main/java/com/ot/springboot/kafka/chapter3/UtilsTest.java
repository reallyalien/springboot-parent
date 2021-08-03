package com.ot.springboot.kafka.chapter3;

import org.apache.kafka.common.utils.Utils;

public class UtilsTest {


    public static void main(String[] args) {
        byte[] bytes = {1,2,10};
        int positive = Utils.toPositive(Utils.murmur2(bytes));
        int i =  positive% 3;
        System.out.println(i);
    }

}
