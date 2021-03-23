package com.ot.springboot.aop.processor;

import com.ot.springboot.aop.anno.Hello;
import com.ot.springboot.aop.service.AopService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Component
public class HelloProcessor implements BeanPostProcessor, ApplicationContextAware {

    @Autowired
    private AnnotationConfigServletWebServerApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object service = null;
        if (bean instanceof AopService) {
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Hello.class)) {
                    service = Proxy.newProxyInstance(bean.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            String arg = (String) args[0];
                            arg = arg + "代理参数";
                            Object invoke = method.invoke(bean, new Object[]{arg});
                            return invoke;
                        }
                    });
                    ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
                    beanFactory.registerSingleton(beanName, service);
                }
            }
        }
        return service;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AnnotationConfigServletWebServerApplicationContext) applicationContext;
    }
}
