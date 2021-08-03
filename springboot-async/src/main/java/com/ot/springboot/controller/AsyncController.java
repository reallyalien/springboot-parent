package com.ot.springboot.controller;

import com.ot.springboot.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aaa")
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/async")
    public String async() {
        return asyncService.async();
    }


    @GetMapping("/sync")
    public void sync() {
        asyncService.sync();
    }

    @GetMapping("/body/{a}")
    public String body(@PathVariable("a") String a) {
        return a;
    }
}
