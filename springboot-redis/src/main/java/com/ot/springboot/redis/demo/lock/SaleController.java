package com.ot.springboot.redis.demo.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.locks.Lock;

@Component
public class SaleController {
    private static long count = 100;
    @Autowired
    @Qualifier(value = "redisLock")
    private Lock lock;


    @GetMapping("/sale")
    public Long sale() {
        count=100;
        System.out.println("-------共100张票，分五个窗口开售-------");
        new PlusThread().start();
        new PlusThread().start();
        new PlusThread().start();
        new PlusThread().start();
        new PlusThread().start();
        return count;
    }

    public class PlusThread extends Thread {
        private int amount = 0;//抢多少张票

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "开始售票");
            while (count > 0) {
                lock.lock();
                try {
                    if (count > 0) {
                        amount++;
                        count--;
                    }
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "售出了" + amount + "票");
        }
    }
}
