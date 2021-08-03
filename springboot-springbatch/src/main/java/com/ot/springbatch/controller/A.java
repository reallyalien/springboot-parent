package com.ot.springbatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
public class A {

    @GetMapping("/1")
    public String a() {

        try {
            throw new RuntimeException("1");
        } catch (Exception e) {
            System.out.println(e);
        } finally {

        }
        return "1";
    }
}
