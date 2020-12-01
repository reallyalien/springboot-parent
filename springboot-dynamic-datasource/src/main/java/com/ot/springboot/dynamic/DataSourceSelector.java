package com.ot.springboot.dynamic;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataSourceSelector {

    DynamicDataSourceEnum value() default DynamicDataSourceEnum.MASTER;

    boolean clear() default true;

    /**
     * 注解反编译之后，实际上是一个继承Annotation的接口
     *
     * public interface com.ot.springboot.dynamic.DataSourceSelector extends java.lang.annotation.Annotation {
     *   public abstract com.ot.springboot.dynamic.DynamicDataSourceEnum value();
     *
     *   public abstract boolean clear();
     * }
     */
}
