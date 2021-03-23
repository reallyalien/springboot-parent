package com.ot.springboot.anno;

import com.ot.springboot.config.Config;
import com.ot.springboot.processor.MyAsyncAnnotationProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = Config.class)
public @interface MyEnableAsync {

}
