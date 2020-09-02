//package com.ot.springboot.redis.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.ot.springboot.redis.domain.User;
//import com.ot.springboot.redis.utils.RedisUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@RestController
//public class RedisController {
//
//    private static final String list_key = "user:user1";
//    private static final String str_key = "user:user2";
//
////    @Autowired
////    private RedisTemplate redisTemplate;
////    @Autowired
////    private RedisUtil redisUtil;
//
//    @GetMapping("/test")
//    public String test() {
//        return redisTemplate.toString();
//    }
//
//    @GetMapping("/listAdd")
//    public String listAdd() {
//        List<User> list = new ArrayList<>();
//        list.add(new User(1, "jack"));
//        list.add(new User(1, "小明"));
//        list.add(new User(1, "小王"));
//        redisTemplate.opsForList().rightPush(list_key, list);
//        return "success";
//    }
//
//    @GetMapping("/listGet")
//    public List<User> listGet() {
//        List<User> list = (List<User>) redisTemplate.opsForList().leftPop(list_key);
//        return list;
//    }
//
//    @GetMapping("/strAdd")
//    public String strAdd() {
//        List<User> list = new ArrayList<>();
//        list.add(new User(1, "jack"));
//        list.add(new User(1, "小明"));
//        list.add(new User(1, "小王"));
//        redisTemplate.opsForValue().set(str_key, JSON.toJSONString(list));
//        return "success";
//    }
//
//    @GetMapping("/strGet")
//    public List<User> strGet() {
//        String string = (String) redisTemplate.opsForValue().get(str_key);
//        List<User> list = JSONArray.parseArray(string, User.class);
//        return list;
//    }
//
//}
