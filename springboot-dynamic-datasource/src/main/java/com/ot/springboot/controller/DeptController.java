package com.ot.springboot.controller;

import com.ot.springboot.entities.Dept;
import com.ot.springboot.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/findAll")
    public List<Dept> findAll() {
        List<Dept> all = deptService.findAll();
        Dept byId = deptService.findById();
        return all;
    }

    @GetMapping("/findBean")
    public String[] findBeanName() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        return beanDefinitionNames;
    }
}
