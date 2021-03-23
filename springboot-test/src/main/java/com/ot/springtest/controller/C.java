package com.ot.springtest.controller;

import com.ot.springtest.dto.Pig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class C {

    @GetMapping("/test")
    public String test() {
        try {
            hello();
            return "success";
        } catch (Exception e) {
            System.out.println(e.toString());
            return "error";
        }
    }

    public void hello() {
        try {
            int a = 1 / 0;
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        }
    }
    @GetMapping("/get")
    public Pig get(){
        return new Pig("pig",10);
    }
}
