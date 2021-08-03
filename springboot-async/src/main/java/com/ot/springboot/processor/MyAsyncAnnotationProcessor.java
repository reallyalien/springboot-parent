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
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

public class MyAsyncAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {

    private List<String> methodNameList = new CopyOnWriteArrayList<>();

    private AnnotationConfigServletWebServerApplicationContext ac;

    private ThreadPoolTaskExecutor executor;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 此方法返回null后置处理器便不再处理
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object instance = null;
        if (!(bean instanceof AsyncService)) {
            return bean;
        }
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(MyAsync.class)) {
                methodNameList.add(method.getName());
                instance = getProxyBean(bean);
                ConfigurableListableBeanFactory beanFactory = ac.getBeanFactory();
                //在后置处理器没有处理完之前，当前bean对象不会放在singletonObject当中
                beanFactory.registerSingleton(beanName, instance);
                //找到一个就可以生成代理对象并放入spring容器然后退出
                break;
            }
        }
        return instance;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = (AnnotationConfigServletWebServerApplicationContext) applicationContext;
        this.executor = this.ac.getBean(ThreadPoolTaskExecutor.class);
    }

    public Object getProxyBean(Object bean) {
//        byte[] bytes = ProxyGenerator.generateProxyClass("AsyncServiceProxy", new Class[]{bean.getClass()});
//        RandomAccessFile r = null;
//        String filePath = "D:/java/project/springboot-parent/springboot-async/src/main/java/com/ot/springboot/service/AsyncServiceProxy.class";
//        try {
//            r = new RandomAccessFile(new File(filePath), "rw");
//            r.write(bytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                r.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result = null;
                if (methodNameList.contains(method.getName())) {
                    //将方法放入线程池当中去执行
                    Future<Object> future = executor.submit(() -> method.invoke(bean, args));
                    //获取方法执行的结果返回
                    result = future.get();
                } else {
                    result = method.invoke(bean, args);
                }
                return result;
            }
        });
    }
}
