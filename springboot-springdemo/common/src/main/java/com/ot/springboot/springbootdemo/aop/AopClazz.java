//package com.ot.springboot.springbootdemo.aop;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class AopClazz {
//
////    @Pointcut("@annotation(com.ot.springboot.springbootdemo.anno.Node)")
////    public void point(){}
//
//
//    @Pointcut("execution( * com.ot.springboot.springbootdemo.*.*(..))")
//    public void point(){}
//
//    @Around(value ="point()" )
//    public Object around(ProceedingJoinPoint pjp){
//        Object arg = pjp.getArgs()[0];
//        System.out.println(arg);
//        try {
//            return pjp.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return null;
//    }
//}
