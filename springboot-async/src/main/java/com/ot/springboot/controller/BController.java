package com.ot.springboot.controller;

import com.ot.springboot.service.BService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/B")
public class BController {


    @Autowired
    private BService bService;

    @GetMapping("/b")
    public void b() {
        bService.test();
    }
}
