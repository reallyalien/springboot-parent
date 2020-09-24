package com.ot.springboot.redis.demo.server;

import redis.clients.jedis.Jedis;

public class RedisClient {

    public static void main(String[] args) {
        Jedis jedis=new Jedis("127.0.0.1",6379);
        jedis.set("k1","v1");
        //aof文件保存格式，resp协议
        /**
         * *3
         * $3
         * SET
         * $2
         * k1
         * $2
         * v1
         */
    }
}
