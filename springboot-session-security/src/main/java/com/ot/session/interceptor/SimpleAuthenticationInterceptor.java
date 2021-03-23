package com.ot.session.interceptor;


import com.ot.session.model.UserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class SimpleAuthenticationInterceptor implements HandlerInterceptor {
    /**
     * 请求拦截方法
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object object = request.getSession().getAttribute(UserDto.SESSION_USER_KEY);
        if (object == null) {
            writeContent(response, "请登录");
        }
        String requestURI = request.getRequestURI();
        UserDto user = (UserDto) object;
        if (user.getAuthorities().contains("p1") && requestURI.contains("/r1")) {
            return true;
        }
        if (user.getAuthorities().contains("p2") && requestURI.contains("/r2")) {
            return true;
        }
        writeContent(response, "权限不足，拒绝访问");
        return false;

    }

    //响应输出
    private void writeContent(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(msg);
        writer.close();
    }
}
