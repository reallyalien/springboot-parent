package com.springboot.redis.redisTest;

import com.springboot.redis.JedisClusterFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ClusterTest {

    @Autowired
    private JedisClusterFactory jedisClusterFactory;


    @Test
    public void method() throws Exception {
        JedisCluster jedisCluster = jedisClusterFactory.getObject();
        jedisCluster.set("a","a1");
        System.out.println(jedisCluster.get("a"));
    }
}
