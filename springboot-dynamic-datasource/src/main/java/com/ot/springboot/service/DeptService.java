package com.ot.springboot.service;

import com.ot.springboot.entities.Dept;

import java.util.List;

public interface DeptService {

    Dept findByIdM();

    Dept findByIdS();

    void insert(Dept dept);

}
