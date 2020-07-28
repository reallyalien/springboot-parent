package com.ot.springboot.aop.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class MyAdvice {

    private static ThreadLocal<Object> local = new ThreadLocal<>();

    //前置，最终肯定会执行，方法正常结束:后置通知，异常结束:异常通知
    @Before("pc()")
    public void before() {
        System.out.println("前置通知");
    }

    @After(("pc()"))
    public void after1() {
        System.out.println("最终通知1"+local.get());
    }

    @After(("pc()"))
    public void after2() {
        int a=1/0;
        System.out.println("最终通知2"+local.get());
    }

    @After(("pc()"))
    public void after3() {
        System.out.println("最终通知3"+local.get());
    }

    //================================================================================================================================
    @AfterReturning("pc()")
    public void afterReturn() {
        System.out.println("后置通知");
    }

    @AfterThrowing("pc()")
    public void afterThrow() {
        System.out.println("异常通知");
    }



    /* */

    /**
     * @param
     * @return controller调service方法的返回值
     *//*
    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp) {
        Object proceed = null;
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
            proceed = pjp.proceed(args);
            System.out.println("环绕通知放行之后的返回值：" + proceed);
        } catch (Throwable e) {
            System.out.println("捕获到异常");
        } finally {
            System.out.println("finally执行了");
        }
        return proceed;
    }*/
    @Pointcut("execution(* com.ot.springboot.aop.service.*.*(..))")
    public void pc() {
    }

    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp) {
        Object proceed = null;
        try {
            //得到当前方法参数的数组
            Object[] args = pjp.getArgs();
            local.set(args[0]);
            pjp.proceed();
        } catch (Throwable e) {

        } finally {

        }
        return proceed;
    }

}
