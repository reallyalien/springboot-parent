package com.ot.springboot.filter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilterController {


    @GetMapping("/filter")
    public String filter(){
        System.out.println("controller执行");
        return "111";
    }
}
