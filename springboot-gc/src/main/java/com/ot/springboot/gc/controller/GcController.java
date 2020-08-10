package com.ot.springboot.gc.controller;

import com.ot.springboot.gc.domain.TestObj;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@RestController
@Slf4j
public class GcController {

    //用来模拟老年代的对象，当对象池大小达到20w的时候清空一次
    private Queue<TestObj> queue = new ConcurrentLinkedDeque<>();

    @GetMapping("/test")
    public TestObj test() {
        TestObj testObj = new TestObj("hello world");
        if (queue.size() >= 200_000) {
            queue.clear();
        } else {
            queue.offer(testObj);
        }
        return testObj;
    }

    /**
     * 默认tomcat开启200个work thread来处理请求
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

}
