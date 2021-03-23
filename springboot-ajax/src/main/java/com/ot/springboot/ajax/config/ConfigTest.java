package com.ot.springboot.ajax.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigTest {

    private static final Map<String, Object> map = new ConcurrentHashMap<>(256);

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> clazz = Class.forName("com.ot.springboot.ajax.config.CorsConfig");
        Object instance = clazz.newInstance();
        Annotation[] annotations = clazz.getAnnotations();
        Annotation annotation = annotations[0];
        if (annotation instanceof Configuration) {
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    String name = method.getName();
                    method.setAccessible(true);
                    Parameter[] parameters = method.getParameters();
                    Object invoke = method.invoke(instance, parameters[0].getName());
                    map.put(name,invoke);
                    System.out.println(map);
                }
            }
        }
    }
}
