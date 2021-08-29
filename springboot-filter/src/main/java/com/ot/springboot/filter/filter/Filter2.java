package com.ot.springboot.filter.filter;


import javax.servlet.*;
import java.io.IOException;

public class Filter2 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("filter2 init");
    }

    @Override
    public void destroy() {
//        System.out.println("filter2 destroy");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(Filter2.class.getName() + "前执行了");
        chain.doFilter(request, response);
        System.out.println(Filter2.class.getName() + "后执行了");
    }
}
