package com.ot.springboot.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class LoginController {

    @RequestMapping(value = "/login-success")
    public String loginSuccess() {
        String username = getUsername();
        return username + "登录成功";
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
    public String r1() {
        return " 访问资源1";
    }

    /**
     * 测试资源2
     *
     * @return
     */
    @GetMapping(value = "/r/r2")
    public String r2() {
        return " 访问资源2";
    }

    /**
     * 测试资源3
     *
     * @return
     */
    @GetMapping(value = "/r/r3")
    public String r3() {
        return " 访问资源3";
    }


    @GetMapping(value = "/test")
    public String test() {
        return "不登陆就可以访问";
    }

    @GetMapping(value = "/test1")
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    public String test1() {
        return "不登陆就可以访问";
    }

    @GetMapping(value = "/test1")
    @PreAuthorize("isAnonymous()")
    public String test2() {
        return "不登陆就可以访问";
    }
}
