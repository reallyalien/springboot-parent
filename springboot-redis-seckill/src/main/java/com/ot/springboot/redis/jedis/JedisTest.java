package com.ot.springboot.redis.jedis;

import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import redis.clients.jedis.Jedis;
import sun.nio.ch.ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class JedisTest {

    private static ExecutorService executor = Executors.newFixedThreadPool(200);

    private static Jedis jedis = new Jedis("127.0.0.1", 6379);

    public static void main(String[] args) {
        for (int i = 0; i < 200; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        //每次命令都会创建socket tcp连接，有可能线程1创建了socket设置好参数之后，连接好之后，线程2又初始化了socket，这样线程1
                        //获取redisInputStream就会报错，socket已关闭
                        jedis.set("hello", "world");
                    }
                }
            });
        }
    }
}
