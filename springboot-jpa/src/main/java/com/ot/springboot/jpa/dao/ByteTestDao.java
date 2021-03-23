package com.ot.springboot.jpa.dao;


import com.ot.springboot.jpa.entity.ByteTest;
import com.ot.springboot.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 泛型：操作对象，主键类型
 */
@Repository
public interface ByteTestDao extends JpaRepository<ByteTest,Long>, JpaSpecificationExecutor<ByteTest> {


   @Query(value="select  * from byte_test where id =1 ",nativeQuery=true)
   Map<String,Object> findMap(Long id);
}
