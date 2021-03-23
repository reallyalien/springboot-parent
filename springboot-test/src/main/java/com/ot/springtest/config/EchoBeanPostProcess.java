package com.ot.springtest.config;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.List;


//实现BeanPostProcessor接口的类，放入spring容器中的后置处理器当中，容器启动和关闭时会执行以下两个重写的方法
public class EchoBeanPostProcess implements BeanPostProcessor {

    private List<String> packages;

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    /**
     * 添加的BeanPostProcess会被所有bean共享，因此下面才要if判断，是处理的当前bean才进继续，否则return null,在后置处理器
     * 的调用链当中，一旦返回null，后置处理器前置调用会停止，会继续执行init的一些方法，接着执行后置处理器的后置方法
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (String pack : packages) {
            if (bean.getClass().getName().startsWith(pack)) {
                System.out.println("echo bean:" + bean.getClass().getName());
            }
        }
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
