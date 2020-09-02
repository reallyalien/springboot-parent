package com.ot.springboot.redis.demo.lock;

import com.ot.springboot.redis.utils.FileUtils;
import com.ot.springboot.redis.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
public class RedisLock implements Lock {
    private static final String KEY = "LOCK_KEY";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    @Autowired
    private JedisUtils jedisUtils;
    private static ThreadLocal<String> pool = new ThreadLocal<>();

    /**
     * 阻塞式加锁
     */
    @Override
    public void lock() {
        if (tryLock()) {
            return;
        }
        //加锁失败，当前任务休眠一段时间
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //递归调用
        lock();
    }

    /**
     * 阻塞式加锁
     *
     * @return
     */
    @Override
    public boolean tryLock() {
        //生成随机值
        String uuid = UUID.randomUUID().toString();
        String state = jedisUtils.setnx(KEY, uuid, 1000L);
        //如果设置成功
        if ("OK".equals(state)) {
            pool.set(uuid);//抢锁成功，将标识号存入当前线程
            return true;
        }
        //抢锁失败，返回false
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        String script = FileUtils.getScript("unlock.lua");
        Object eval = jedisUtils.eval(script, Collections.singletonList(KEY), Collections.singletonList(pool.get()));
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
}
