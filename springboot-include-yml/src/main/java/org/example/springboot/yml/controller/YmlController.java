package org.example.springboot.yml.controller;

import org.example.springboot.yml.config.DemoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yml")
public class YmlController {


    @Autowired
    private DemoConfig demoConfig;

    @GetMapping
    public void test(){
        System.out.println();
    }
}
