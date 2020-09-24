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

@Aspect
@Component
public class AopClazz {

    @Pointcut("@annotation(com.ot.springboot.springbootdemo.anno.Node)")
    public void point() {
    }
    @Autowired
    private AopClazz aopClazz;


//    @Pointcut("execution( * com.ot.springboot.springbootdemo.*.*(..))")
//    public void point(){}

    @Autowired
    private AopService aopService;

    @Around(value = "point()")
    public Object around(ProceedingJoinPoint pjp) {
        System.out.println("aop线程："+Thread.currentThread().getName());
        Object proceed = null;
        Object arg = pjp.getArgs()[0];
        long start = System.currentTimeMillis();
        System.out.println("aop执行前");
        try {
            proceed = pjp.proceed();
//            aopService.sleep();
            aopClazz.sleep();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("aop执行后，耗时：" + (end - start));
        return proceed;
    }

    @Async
    public void sleep(){
        System.out.println("当前线程sleep："+Thread.currentThread().getName());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
