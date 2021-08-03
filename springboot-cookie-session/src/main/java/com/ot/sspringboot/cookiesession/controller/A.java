package com.ot.sspringboot.cookiesession.controller;

import java.lang.reflect.Field;

public class A {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Integer i=10;
        System.out.println(i.hashCode());
        Class<Integer> clazz = Integer.class;
        Field value = clazz.getDeclaredField("value");
        value.setAccessible(true);
        value.setInt(i,9);
        System.out.println(i);
        System.out.println(i.hashCode());
    }
}
