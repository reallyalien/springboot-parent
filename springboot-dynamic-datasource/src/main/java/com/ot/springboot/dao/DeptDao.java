package com.ot.springboot.dao;

import com.ot.springboot.entities.Dept;

import java.util.List;

public interface DeptDao {


    public List<Dept> findAll();

    public Dept findById();

    public void insert(Dept dept);
}
