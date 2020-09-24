package com.springboot.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

public class JedisCluster {
    private static redis.clients.jedis.JedisCluster jedisCluster = null;

    static {
        Set nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.88.128", 7001));
        nodes.add(new HostAndPort("192.168.88.128", 7002));
        nodes.add(new HostAndPort("192.168.88.128", 7003));
        nodes.add(new HostAndPort("192.168.88.128", 7004));
        nodes.add(new HostAndPort("192.168.88.128", 7005));
        nodes.add(new HostAndPort("192.168.88.128", 7006));
        jedisCluster = new redis.clients.jedis.JedisCluster(nodes,200000,200000,20000,new GenericObjectPoolConfig());
    }

    public static void main(String[] args) {
        jedisCluster.set("k1","v1");
        System.out.println(jedisCluster.get("k1"));
    }
}
