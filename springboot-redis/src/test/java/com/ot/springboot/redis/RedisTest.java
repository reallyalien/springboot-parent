package com.ot.springboot.redis;


import com.ot.springboot.redis.domain.User;
import com.ot.springboot.redis.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = RedisUtil.class)
public class RedisTest {

    private static final String key="user:user1";

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void listTest(){
        List<User> list=new ArrayList<>();
        list.add(new User(1,"jack"));
        list.add(new User(1,"小明"));
        list.add(new User(1,"小王"));
        redisUtil.lSet(key,list);
    }
}
