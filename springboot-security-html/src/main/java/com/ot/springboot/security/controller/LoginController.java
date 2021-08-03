package com.ot.springboot.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 * @version 1.0
 **/
@Controller
public class LoginController {

    @RequestMapping(value = "/login-success")
    @ResponseBody
    public String loginSuccess(HttpSession session) {
        System.out.println("登录成功："+session.getId());
        String username = getUsername();
        return username + "登录成功";
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        System.out.println("首页："+session.getId());
        return "redirect:/login.html";
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            UserDetails details = (UserDetails) principal;
            username = details.getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    /**
     * 测试资源1
     *
     * @return
     */
    @GetMapping(value = "/r/r1")
    @ResponseBody
    public String r1(HttpSession session) {
        System.out.println("访问资源1:"+session.getId());
        return " 访问资源1";
    }

    /**
     * 测试资源2
     *
     * @return
     */
    @GetMapping(value = "/r/r2")
    @ResponseBody
    public String r2() {
        return " 访问资源2";
    }

    /**
     * 测试资源3
     *
     * @return
     */
    @GetMapping(value = "/r/r3")
    @ResponseBody
    public String r3() {
        return " 访问资源3";
    }

    @GetMapping("/cookie")
    @ResponseBody
    public String cookieHttpOnly(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("a", "A");
        response.addCookie(cookie);
        return "success";
    }


    @GetMapping(value = "/test")
    @ResponseBody
    public String test() {
        return "不登陆就可以访问";
    }

    @GetMapping(value = "/test1")
    @ResponseBody
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    public String test1() {
        return "不登陆就可以访问";
    }

    @GetMapping(value = "/test2")
    @ResponseBody
    @PreAuthorize("isAnonymous()")
    public String test2() {
        return "不登陆就可以访问";
    }

    @GetMapping(value = "/test3")
    @ResponseBody
    @Secured("ROLE_ADMIN")
    public String test3() {
        return "不登陆就可以访问";
    }
}
