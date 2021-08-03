package com.ot.sspringboot.cookiesession.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class HelloController {


    @GetMapping("/cookie")
    public String cookie(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Cookie cookie = new Cookie("a", "A");
        Cookie cookie1 = new Cookie("b", "B");
        //设置此属性，前端无法获取cookie
//        cookie.setHttpOnly(true);
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
        response.addCookie(cookie1);
        return "111";
    }

    @GetMapping("/session")
    public String session(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        HttpSession httpSession = request.getSession();
        //此session与参数当中的是同一个session实现都是StandardSessionFacade
        return "111";
    }
    @GetMapping("/token")
    public String token(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        request.setAttribute("a","A");
        return null;
    }

}
