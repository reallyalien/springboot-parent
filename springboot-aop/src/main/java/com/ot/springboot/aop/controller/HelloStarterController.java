package com.ot.springboot.aop.controller;

import com.ot.springboot.aop.domain.User;
import com.ot.springboot.hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloStart")
public class HelloStarterController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello")
    public String hello() {
        User user = new User();
        user.setAge(10);
        user.setName("傻瓜");
        return helloService.objToJson(user);
    }

}
