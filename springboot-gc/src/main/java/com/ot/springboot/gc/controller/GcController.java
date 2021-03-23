package com.ot.springboot.gc.controller;

import com.ot.springboot.gc.domain.TestObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@RestController
@Slf4j
public class GcController {

    //用来模拟老年代的对象，当对象池大小达到20w的时候清空一次
    private Queue<TestObj> queue = new ConcurrentLinkedDeque<>();

    private List<Object> list = new LinkedList<>();

    /**
     * -Xmx2048m
     * -Xss256k
     * -verbose:gc
     * -Xloggc:gc.log
     * -XX:+PrintGCDetails
     * -XX:+PrintGCTimeStamps
     * -XX:+PrintGCDateStamps
     * -XX:+PrintReferenceGC
     * -XX:+PrintTenuringDistribution
     * -XX:+PrintGCApplicationStoppedTime
     * -XX:+UseGCLogFileRotation
     * -XX:NumberOfGCLogFiles=2
     * -XX:GCLogFileSize=100M
     *
     * @return
     */
    @GetMapping("/test")
    public TestObj test() {
        TestObj testObj = new TestObj("hello world");
        if (queue.size() >= 200_000) {
            queue.clear();
        } else {
            for (int i = 0; i < 10; i++) {
                queue.offer(testObj);
            }
        }
        return testObj;
    }

    /**
     * 默认tomcat开启200个work thread来处理请求
     *
     * @param ms
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/hello/{ms}")
    public String hello(@PathVariable("ms") Integer ms) throws InterruptedException {
        log.info(Thread.currentThread().getName() + "\t当前开始时间：" + new Date().toLocaleString());
        Thread.sleep(ms * 1000);
        log.info(Thread.currentThread().getName() + "\t当前结束时间：" + new Date().toLocaleString());
        return "hello";
    }

    /**
     * -verbose:gc
     * -Xloggc:gc.log
     * -XX:+PrintGCDetails
     *
     * @return
     */
    @Scheduled(fixedDelay = 1000)
    public String outOfMemory() {
        for (int i = 0; i < 50000; i++) {
            list.add(new Object());
        }
        return "success";
    }

}
