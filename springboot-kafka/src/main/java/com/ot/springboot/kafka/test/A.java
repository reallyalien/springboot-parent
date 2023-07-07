package com.ot.springboot.kafka.test;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class A {

    public static void main(String[] args) {
        String a = "{\"name\":\"1这是json格式的数据\",\"id\":1}";
        String b = "{\"a\":\"A\",\"b\":\"B\"}";
        String c = "\"{\\\"name\\\":\\\"6这是json格式的数据\\\",\\\"id\\\":6}\"";
        Map<String, Object> map = new HashMap<>();
        map.put("a", "A");
        map.put("b", "B");
        String s = JSON.toJSONString(map);
        System.out.println(s);
        //==========
        Map map1 = JSON.parseObject(a, Map.class);
        System.out.println(map1);
    }
}
