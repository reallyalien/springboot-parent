package com.ot.springboot.jpa;


import com.ot.springboot.jpa.dao.CustomerDao;
import com.ot.springboot.jpa.dao.LinkManDao;
import com.ot.springboot.jpa.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = JpaMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class JPATest {

    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private LinkManDao linkManDao;
    @Test
    public void t() {
        System.out.println(customerDao);
    }

//    @Test
//    public void insert(){
//        Customer customer = new Customer();
//        customer.setCustName("百度");
//
//        LinkMan linkMan1 = new LinkMan();
//        linkMan1.setLkmName("周杰伦他妈");
//
//        LinkMan linkMan2 = new LinkMan();
//        linkMan2.setLkmName("周杰伦他爸");
//
//        //告诉客户有哪些联系人
//        customer.getLinkmens().add(linkMan1);
//        customer.getLinkmens().add(linkMan2);
//
//        //告诉联系人属于哪个客户
//        linkMan1.setCustomer(customer);
//        linkMan2.setCustomer(customer);
//
//        /**
//         * save方法，当前对象有id，查询并修改，
//         * 没有id，即为插入操作
//         */
//        customerDao.save(customer);
//        linkManDao.save(linkMan1);
//        linkManDao.save(linkMan2);
//    }

    @Test
    public void find() {
        List<Customer> customers = customerDao.findAll();
        customers.forEach(System.out::println);
    }
//    }
//    @Test
//    public void findByCardNumAndName(){
//        Student student = studentService.findByCardNumAndName("ab", "小明");
//        System.out.println(student);
//    }
//
//    @Test
//    public void findByNameLike(){
//        Student student = studentService.getStudentByNameLike("%小%");
//        System.out.println(student);
//    }
//    @Test
//    public void update(){
//        Integer i = studentService.update("jackk", 1);
//        System.out.println("修改的记录是："+i);
//    }

//    @Test
//    @Transactional
//    public void findAll() {
//        List<User> users = userDao.findAll();
//        users.forEach(System.out::println);
//    }
//
//    /**
//     * qbc查询
//     */
    @Test
    @Transactional
    public void findQBC(){
        Specification<Customer> specification = new Specification<Customer>(){
            //root 封装实体类的对象，有此对象之后，任何实体类都可以看成此类型
            //criteria 创建查询对象，用于生成sql语句
            //criteriaBuilder 用于拼接条件查询
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                Predicate p1 = criteriaBuilder.equal(root.get("custId").as(Integer.class), 2);
//                Predicate p2 = criteriaBuilder.like(root.get("custName").as(String.class), "%%");
//                Predicate p3 = criteriaBuilder.and(p1, p2);
//                criteriaQuery.orderBy(new OrderImpl(root.get("custId"), false));
//                criteriaQuery.where(p3);
//                return p3;
                return null;
            }
        };
        ExampleMatcher exampleMatcher=ExampleMatcher.matchingAll().withMatcher("custName", ExampleMatcher.GenericPropertyMatchers.startsWith());
        Customer customer=new Customer();
        customer.setCustName("百度");
        Example<Customer> example=Example.of(customer,exampleMatcher);
//        List<Customer> customers = customerDao.findAll(specification);
        List<Customer> customers = customerDao.findAll(example);
        customers.forEach(n-> System.out.println(n));
    }
//
//    /**
//     * 分页查询
//     */
    @Test
    public void findQBCPage(){
        Specification<Customer> specification = new Specification<Customer>(){
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Date date = new Date();
                long time = date.getTime();
                Expression createTime = root.get("createTime").as(Date.class);
                Predicate p1 = criteriaBuilder.greaterThanOrEqualTo(createTime, time);
                return p1;
            }
        };
        Pageable pageable=PageRequest.of(1-1,10);//参数1 ：第几页 参数二：每页显示几条
        Page<Customer> pages = customerDao.findAll(specification, pageable);
        long total = pages.getTotalElements();
        List<Customer> list = pages.getContent();
        System.out.println(total);
        System.out.println(list);

//        for (Customer user : users) {
//            System.out.println(user);
//        }

    }

    @Test
    public void update(){
        Customer customer = new Customer();
        customer.setCustName("a");
        customer.setCustId(1L);
        customerDao.save(customer);
    }
}

