package com.ot.springboot.service.impl;

import com.ot.springboot.dao.DeptDao;
import com.ot.springboot.dynamic.DataSourceSelector;
import com.ot.springboot.dynamic.DynamicDataSourceEnum;
import com.ot.springboot.entities.Dept;
import com.ot.springboot.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;

    @Override
    @DataSourceSelector(value = DynamicDataSourceEnum.MASTER)
    public Dept findByIdM() {
        return deptDao.findById();
    }

    @Override
    @DataSourceSelector(value = DynamicDataSourceEnum.SLAVE)
    public Dept findByIdS() {
        return deptDao.findById();
    }

    @Override
    public void insert(Dept dept) {
        deptDao.insert(dept);
    }
}
