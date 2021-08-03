package com.ot.springboot.controller;

import com.ot.springboot.entities.Dept;
import com.ot.springboot.service.DeptService;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;


    @GetMapping(value = "/findAll")
    public List<Dept> findAll() {
        Dept byIdM = deptService.findByIdM();
        Dept byIdS = deptService.findByIdS();
        Dept[] arr = new Dept[]{byIdM, byIdS};
        return Arrays.asList(arr);
    }
}
