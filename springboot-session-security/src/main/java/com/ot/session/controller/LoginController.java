package com.ot.session.controller;

import com.ot.session.model.AuthenticationRequest;
import com.ot.session.model.UserDto;
import com.ot.session.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @ResponseBody// produces = {"text/plain;charset=UTF-8"}
    @PostMapping(value = "/login")
    public String login(AuthenticationRequest request, HttpSession session) {
        System.out.println("登录：" + session.getId());
        UserDto userDto = authenticationService.authentication(request);
        session.setAttribute(UserDto.SESSION_USER_KEY, userDto);
        return userDto.getFullname() + "登录成功";
    }

    @ResponseBody
    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        System.out.println("登出：" + session.getId());
        session.invalidate();
        return "退出成功";
    }

    /**
     * 测试资源1
     *
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/r/r1")
    public String r1(HttpSession session) {
        String fullname = null;
        Object userObj = session.getAttribute(UserDto.SESSION_USER_KEY);
        if (userObj != null) {
            fullname = ((UserDto) userObj).getFullname();
        } else {
            fullname = "匿名";
        }
        return fullname + " 访问资源1";
    }

    /**
     * 测试资源2
     *
     * @param session
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/r/r2")
    public String r2(HttpSession session) {
        String fullname = null;
        Object userObj = session.getAttribute(UserDto.SESSION_USER_KEY);
        if (userObj != null) {
            fullname = ((UserDto) userObj).getFullname();
        } else {
            fullname = "匿名";
        }
        return fullname + " 访问资源2";
    }


    @GetMapping("/")
    public String index(HttpSession session) {
        System.out.println("首页："+session.getId());
        return "login";
    }
}
