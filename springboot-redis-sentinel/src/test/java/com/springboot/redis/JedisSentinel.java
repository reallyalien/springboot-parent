package com.springboot.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * client 拿着 sentinel 节点集合 + materName，遍历 sentinel 集合，获取一个可用的 sentinel 节点；
 * client 拿着 masterName 向获取到的可用的 sentinel 节点要 master 的地址；
 * client 拿着 master 的地址验证一下其到底是不是 master；
 * 如果 master 发生的转移，sentinel 是可以感知的，client 和 sentinel 之间的通知是通过发布订阅模式，client 订阅了 sentinel 的某个频道，频道中有 master 的变化，如果 master 发生了变化，就会在这个频道中发布一条消息，订阅的 client 就可以获取，在取新的 master 进行连接；
 *
 */
public class JedisSentinel {


    @Test
    public void testJedis() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(10);
        config.setMaxWaitMillis(1000);

        Set<String> sentinels = new HashSet<>();
        String hostAndPort1 = "192.168.106.139:26379";
        String hostAndPort2 = "192.168.106.139:26380";
        String hostAndPort3 = "192.168.106.139:26381";
        sentinels.add(hostAndPort1);
        sentinels.add(hostAndPort2);
        sentinels.add(hostAndPort3);

        String clusterName = "mymaster";
        String password="root";
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(clusterName, sentinels,config,1000,password);
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            jedis.set("k2","v1");
            System.out.println(jedis.get("k1"));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        jedisSentinelPool.close();
    }
}
