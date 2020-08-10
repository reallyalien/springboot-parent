package com.ot.springboot.gc.controller;

import com.ot.springboot.gc.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private Executor executor;

    @GetMapping("/async")
    public String async(){
        asyncService.runTask();
        System.out.println(executor);
        return "success";
    }
}
