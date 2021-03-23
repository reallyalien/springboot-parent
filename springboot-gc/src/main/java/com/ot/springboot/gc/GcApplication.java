package com.ot.springboot.gc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;

@SpringBootApplication(exclude = TaskSchedulingAutoConfiguration.class)
//@EnableScheduling
public class GcApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GcApplication.class);
        ConfigurableApplicationContext ac = springApplication.run(args);
        String[] names = ac.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}

/**
 * 在springboot项目当中，先创建AnnotationWebServletApplicationContext，在IOC容器refresh的时候，onRefresh方法会去创建tomcat服务器
 * 并开启监听，此时创建了tomcat默认的defaultServlet，dispatchServlet已经创建，尚未初始化，第一次请求才会初始化
 * <p>
 * 第一次请求的时候，先到StandardWrapperValve的invoke方法，进行dispatcherServlet的init方法，最终调用到frameworkServlet的
 * initWebApplicationContext方法，获取rootApplicationContext，就是springboot刚开始创建的IOC容器，SpringBoot与springMVC不同的就是这里
 * 通用一个IOC容器，没有区分springIOC和DispatcherServletIOC容器，在springMVC项目当中是在IOC容器启动结束之后的publishEvent事件当中去
 * 调用dispatcherServlet的onRefresh方法，而springBoot不同的是通过抽象方法frameworkServlet去调用子类DispatchServlet的onRefresh方法
 * 初始化HandlerMapping和HandlerAdapter，拦截器并没有使用AOP，就是一个拦截器的数组，遍历依次调用，只要有一个返回false，请求中止
 *
 * DispatcherServletAutoConfiguration自动装配创建servlet->DispatcherServletRegistrationBean-->在servletRegisterBean当中把dispatcherServlet交给了StandardWrapper
 */
