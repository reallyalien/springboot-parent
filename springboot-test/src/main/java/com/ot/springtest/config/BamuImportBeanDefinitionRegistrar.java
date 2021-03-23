package com.ot.springtest.config;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BamuImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * AnnotationConfigurationApplicationContext实现了BeanDefinitionRegistry，其实可以理解为BeanFactory
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获取@EnableEcho注解的所有属性的value
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableEcho.class.getName());
        List<String> packages = Arrays.asList((String[]) annotationAttributes.get("packages"));
        //利用registry对象将EchoBeanPostProcess对象注入spring容器
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(EchoBeanPostProcess.class);
        //给EchoBeanPostProcess注入property属性，在实例创建之后，先进行属性装配，再执行初始化
        beanDefinitionBuilder.addPropertyValue("packages", packages);
        registry.registerBeanDefinition(EchoBeanPostProcess.class.getName(), beanDefinitionBuilder.getBeanDefinition());
    }
}
