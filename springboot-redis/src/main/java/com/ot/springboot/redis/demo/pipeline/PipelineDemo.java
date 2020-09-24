package com.ot.springboot.redis.demo.pipeline;

import com.ot.springboot.redis.utils.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PipelineDemo {
    private static JedisUtils jedisUtils = new JedisUtils();
    // 并发任务
    private static final int taskCount = 50;
    // pipeline大小
    private static final int batchSize = 10;
    // 每个任务处理命令数
    private static final int cmdCount = 2000;

    private static final boolean usePipeline = true;

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService threadPool = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(taskCount);
        for (int i = 0; i < taskCount; i++) {
            threadPool.execute(new DemoTask(i, countDownLatch));
        }
        countDownLatch.await();
        threadPool.shutdownNow();
        long end = System.currentTimeMillis();
        System.out.println((end - start) / 1000);
    }


    public static class DemoTask implements Runnable {

        private int i;
        private CountDownLatch countDownLatch;

        public DemoTask(int i, CountDownLatch countDownLatch) {
            this.i = i;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                if (usePipeline) {
                    runWithPipeline();
                } else {
                    runNoPipeline();
                }
            } catch (Exception e) {

            } finally {
                countDownLatch.countDown();
            }
        }

        public void runWithPipeline() {
            JedisUtils jedisUtils = new JedisUtils();
            Jedis jedis = jedisUtils.jedis();
            Pipeline pipelined = jedis.pipelined();
            for (int j = 0; j < cmdCount; j++) {
                pipelined.set("key" + i + j, UUID.randomUUID().toString());

            }
            pipelined.sync();

        }

        public void runNoPipeline() {
            JedisUtils jedisUtils = new JedisUtils();
            for (int j = 0; j < cmdCount; j++) {
                jedisUtils.set("key" + i + j, UUID.randomUUID().toString());
            }
        }
    }
}
