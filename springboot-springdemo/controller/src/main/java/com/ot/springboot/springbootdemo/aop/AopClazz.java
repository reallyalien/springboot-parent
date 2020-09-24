package com.ot.springboot.springbootdemo.aop;

import com.ot.springboot.springbootdemo.controller.AopController;
import com.ot.springboot.springbootdemo.service.AopService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Aspect
@Component
public class AopClazz {

    private static ExecutorService pool= Executors.newFixedThreadPool(8);
    @Autowired
    private AopService aopService;

    @Pointcut("@annotation(com.ot.springboot.springbootdemo.anno.Node)")
    public void point() {
    }

    @Around(value = "point()")
    public Object around(ProceedingJoinPoint pjp) throws ExecutionException, InterruptedException {
//        System.out.println("aop线程：" + Thread.currentThread().getName());
//        Object proceed = null;
//        Object arg = pjp.getArgs()[0];
//        long start = System.currentTimeMillis();
//        System.out.println("aop执行前");
//        try {
//            proceed = pjp.proceed();
//            aopService.sleep();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("aop执行后，耗时：" + (end - start));
//        return proceed;
        Callable<Object> task = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    Object result= pjp.proceed();
                    return result;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }
        };
        //@Async就是这个原理
        //任务不提交,放行的方法不会执行,虽然放行的方法跟controller的方法不在一个线程当中,当这个get方法会阻塞,直到拿到结果
        Future<Object> submit = pool.submit(task);
        return submit.get();
    }

    @Async
    public void sleep() {
        System.out.println("当前线程sleep：" + Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
