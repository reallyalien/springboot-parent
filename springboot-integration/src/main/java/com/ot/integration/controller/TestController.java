package com.ot.integration.controller;

import com.ot.integration.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private SendService sendService;
    /**
     * 测试传递dto参数
     */
    @GetMapping("/test1")
    public void test1() {
        sendService.test1();
    }

}
