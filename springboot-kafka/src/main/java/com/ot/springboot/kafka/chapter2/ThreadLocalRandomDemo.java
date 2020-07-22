package com.ot.springboot.kafka.chapter2;

import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomDemo {

    public static void main(String[] args) {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        System.out.println(String.format("%f",100*100)+"%");
    }
}
