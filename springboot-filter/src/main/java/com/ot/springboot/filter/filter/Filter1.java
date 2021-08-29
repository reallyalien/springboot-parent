package com.ot.springboot.filter.filter;


import org.apache.catalina.core.StandardWrapper;

import javax.servlet.*;
import java.io.IOException;

public class Filter1 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("filter1 init");
    }

    @Override
    public void destroy() {
//        System.out.println("filter1 destroy");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(Filter1.class.getName() + "前执行了");
        chain.doFilter(request, response);
        System.out.println(Filter1.class.getName() + "后执行了");
    }
}
