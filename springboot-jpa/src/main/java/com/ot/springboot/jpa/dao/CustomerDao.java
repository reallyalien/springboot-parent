package com.ot.springboot.jpa.dao;


import com.ot.springboot.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 泛型：操作对象，主键类型
 */
@Repository
public interface CustomerDao extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {
}
