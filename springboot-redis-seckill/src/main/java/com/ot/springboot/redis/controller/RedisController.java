package com.ot.springboot.redis.controller;

import com.ot.springboot.redis.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SecKillService secKillService;

    @GetMapping("/test1")
    public void test1() {
        redisTemplate.opsForValue().set("a", "A");
        Object a = redisTemplate.opsForValue().get("a");
        System.out.println();
    }

    /**
     *  ab -n 1000 -c 100 http://192.168.140.1:8080/sec
     */
    @GetMapping("/sec")
    public void secKill() {
        String userId = UUID.randomUUID().toString();
        String prodId = "1";
        secKillService.secKillLua(userId, prodId);
    }
}
