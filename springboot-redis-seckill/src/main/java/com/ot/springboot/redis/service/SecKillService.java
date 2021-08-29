package com.ot.springboot.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class SecKillService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JedisPool jedisPool;

    public boolean secKill(String userId, String productId) {
        if (Objects.isNull(userId) || Objects.isNull(productId)) {
            return false;
        }
        //库存key
        String kcKey = "sk:" + productId + ":qt";
        //用户key
        String userKey = "sk:" + productId + ":user";
        //判断当前用户是否已经参与秒杀
        Boolean isParticipate = redisTemplate.opsForSet().isMember(userKey, userId);
        if (!Objects.isNull(isParticipate) && isParticipate) {
            System.out.println("当前用户：" + userId + " 已经参与过秒杀");
            return false;
        }
        //添加乐观锁
        redisTemplate.watch(kcKey);

        //开始秒杀
        //开启事务
        redisTemplate.multi();
        Object kc = redisTemplate.opsForValue().get(kcKey);
        //库存等于空返回
        if (null == kc || ((Integer) kc) <= 0) {
            System.out.println("库存为空，请稍后再试");
            return false;
        }
        //减库存
        redisTemplate.opsForValue().decrement(kcKey);
        //将用户添加到秒杀列表当中
        redisTemplate.opsForSet().add(userKey, userId);
        //提交事务
        List list = redisTemplate.exec();
        if (list.isEmpty()) {
            System.out.println("秒杀失败");
            return false;
        }
        System.out.println("秒杀成功");
        return true;
    }

    public boolean secKillLua(String userId, String productId) {
        if (Objects.isNull(userId) || Objects.isNull(productId)) {
            return false;
        }
        //库存key
        String kcKey = "sk:" + productId + ":qt";
        //用户key
        String userKey = "sk:" + productId + ":user";
        List<String> keyList = new ArrayList<>();
        keyList.add(userKey);
        keyList.add(kcKey);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua.lua")));
        Long execute = (Long) redisTemplate.execute(redisScript, keyList);
        if (1 == execute) {
            System.out.println("秒杀成功");
            return true;
        }
        System.out.println("秒杀失败");
        return false;
    }
}