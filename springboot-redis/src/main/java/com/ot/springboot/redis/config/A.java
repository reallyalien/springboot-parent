package com.ot.springboot.redis.config;

import com.ot.springboot.redis.domain.User;

import java.util.ArrayList;

public class A {

    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        User user = null;
        String[] str = {"java", "py", "C++"};
        for (int i = 0; i < str.length; i++) {
            user = new User();
            user.setName(str[i]);
            user.setId(i);
            list.add(user);
        }
        System.out.println(list);
    }
}
