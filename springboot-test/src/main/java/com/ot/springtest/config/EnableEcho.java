package com.ot.springtest.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({BamuImportBeanDefinitionRegistrar.class,MyImportSelector.class})
public @interface EnableEcho {

    //传入包名
    String[] packages() default "";
}
