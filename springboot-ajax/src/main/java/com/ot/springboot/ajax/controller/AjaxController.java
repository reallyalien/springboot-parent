package com.ot.springboot.ajax.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.ot.springboot.ajax.domain.User;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/ajax")
public class AjaxController {
    private static Map<String, User> maps = new ConcurrentHashMap<>();

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

    @GetMapping("/cookie")
    public String cookieHttpOnly(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("a", "A");
//        Session.Cookie cookie=new Session.Cookie();
        cookie.setPath("/aaaa");
        cookie.setDomain("/aa");
        return "success";
    }

    @PostMapping("/json")
    public void hello(HttpServletRequest request) throws IOException {
        User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        System.out.println(user);
    }

    @PostMapping("/accept")
    public void accept(@RequestBody User user) throws IOException {
        maps.put("1", user);
        System.out.println(user);
    }

    @GetMapping("/findUser")
    public User findUser() throws IOException {
        User user = maps.get("1");
        System.out.println(user);
        return user;
    }

    @PostMapping("/formData")
    public void formData(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        file.transferTo(new File("d:/1.xlsx"));
        byte[] bytes = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(bytes);

    }


}
