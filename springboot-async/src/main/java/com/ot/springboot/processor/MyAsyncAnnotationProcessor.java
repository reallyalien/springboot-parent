package com.ot.springboot.processor;


import com.ot.springboot.anno.MyAsync;
import com.ot.springboot.service.AsyncService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyAsyncAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {


    private AnnotationConfigServletWebServerApplicationContext ac;

    private ThreadPoolTaskExecutor executor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object instance = null;
        if (bean instanceof AsyncService) {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(MyAsync.class)) {
                    instance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (method.getName().equals("async")) {
                                //这里千万别写return，否则线程任务不执行
                                executor.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            method.invoke(bean, args);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                method.invoke(bean, args);
                            }
                            return null;
                        }
                    });
                    ConfigurableListableBeanFactory beanFactory = ac.getBeanFactory();
                    beanFactory.registerSingleton(beanName, instance);
                }
            }
        }
        return instance;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = (AnnotationConfigServletWebServerApplicationContext) applicationContext;
        this.executor = this.ac.getBean(ThreadPoolTaskExecutor.class);
    }
}
