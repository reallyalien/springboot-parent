package com.ot.springboot.ajax.controller;

import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.ot.springboot.ajax.domain.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/ajax")
public class AjaxController {

    @GetMapping("/hello")
    public String hello(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("my-cookie", "cookie-value");
        //在网页端无法获取通过js脚本获取cookie，避免xss攻击，只能在服务端获取cookie
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        System.out.println("hello");
        return "admin";
    }

    @GetMapping("/hello1")
    public String hello1(HttpServletRequest request, HttpServletResponse response) {
        return "admin1";
    }

    @PostMapping("/arr")
    public void hello(@RequestBody User user) throws IOException {
        FileOutputStream fos = new FileOutputStream("d:/1.txt");
        fos.write(user.getArr());
        fos.close();
    }

}
