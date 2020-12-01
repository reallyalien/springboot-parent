package com.ot.springboot.dynamic;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(-1)//保证在事务注解之前执行
public class DynamicAspect  {

    private static final Logger log = LoggerFactory.getLogger(DynamicAspect.class);

    @Pointcut("@annotation(com.ot.springboot.dynamic.DataSourceSelector)")
    public void pointcut() {
    }


    @Around("pointcut()")
    public Object switchSource(ProceedingJoinPoint pjp) {
        Object o = null;
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            DataSourceSelector annotation = method.getAnnotation(DataSourceSelector.class);
            DynamicDataSourceEnum value = annotation.value();
            DynamicDataSourceContextHolder.set(value.getDataSourceName());
            log.info("数据源切换为：{}", value.getDataSourceName());
            o = pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            DynamicDataSourceContextHolder.clear();
            log.info("清空数据源");
        }
        return o;

    }
}
