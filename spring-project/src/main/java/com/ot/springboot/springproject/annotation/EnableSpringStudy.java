package com.ot.springboot.springproject.annotation;

import com.ot.springboot.springproject.config.SpringStudySelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(SpringStudySelector.class)
public @interface EnableSpringStudy {

}
