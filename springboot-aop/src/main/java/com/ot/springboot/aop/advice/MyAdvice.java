package com.ot.springboot.aop.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MyAdvice {

//    @Before("pc()")
//    public void before() {
//        System.out.println("前置通知");
//    }
//
//    @AfterReturning("pc()")
//    public void afterReturn() {
//        System.out.println("后置通知");
//    }
//
//    @AfterThrowing("pc()")
//    public void afterThrow() {
//        System.out.println("异常通知");
//    }
//
//    @After(("pc()"))
//    public void after() {
//        System.out.println("最终通知");
//    }

    //环绕通知是四大通知的综合，可以切入方法,方法带参数，有返回值
    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp) {
        Object obj = null;
        try {
            //得到当前目标对象的类型
            Class<?> clazz = pjp.getTarget().getClass();
            System.out.println("得到当前目标对象的类型:" + clazz);
            //得到当前切入点方法名
            String name = pjp.getSignature().getName();
            System.out.println("得到当前切入点方法名:" + name);
            //得到当前方法参数的数组
            Object[] args = pjp.getArgs();
            System.out.println("得到当前方法参数的数组：" + Arrays.toString(args));
            Object proceed = pjp.proceed(args);
            System.out.println("环绕通知放行之后的返回值：" + proceed);
        } catch (Throwable e) {

        } finally {

        }
        return "1";
    }

    @Pointcut("execution(* com.ot.springboot.aop.service.*.*(..))")
    public void pc() {
    }
}
