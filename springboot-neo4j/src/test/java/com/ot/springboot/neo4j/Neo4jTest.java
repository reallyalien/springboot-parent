package com.ot.springboot.neo4j;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ot.springboot.neo4j.dao.*;
import com.ot.springboot.neo4j.demo1.dao.ManDao;
import com.ot.springboot.neo4j.demo1.dao.ParentShipDao;
import com.ot.springboot.neo4j.demo1.domain.Man;
import com.ot.springboot.neo4j.demo1.relationship.ParentShip;
import com.ot.springboot.neo4j.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.util.*;

@SpringBootTest
public class Neo4jTest {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private MovieDao movieDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private SupplyDao supplyDao;
    @Autowired
    private SupplyRelationshipDao supplyRelationshipDao;
    @Autowired
    private UserRelationDao userRelationDao;
    @Autowired
    private ShopDao shopDao;
    @Autowired
    private ManDao manDao;
    @Autowired
    private ParentShipDao parentShipDao;
    @Autowired
    private CatDao catDao;

    @Test
    public void saveMovie() {
        Movie movie1 = new Movie("无问东西", "2018");
        Movie movie2 = new Movie("罗曼蒂克消亡史", "2016");
        movieDao.save(movie1);
        movieDao.save(movie2);
    }

    @Test
    public void findMovie() {
        Iterable<Movie> all = movieDao.findAll();
        for (Movie movie : all) {
            System.out.println(movie);
        }
    }

    @Test
    public void savePerson() {
        Person person1 = new Person("章子怡", "1979");
        Person person2 = new Person("李芳芳", "1976");
        Person person3 = new Person("程耳", "1979");
        Movie movie1 = movieDao.findByTitle("罗曼蒂克消亡史");
        Movie movie2 = movieDao.findByTitle("无问东西");
        if (movie1 != null) {
            person1.addActor(movie1);
            person3.addDirectors(movie1);
        }
        if (movie2 != null) {
            person1.addActor(movie2);
            person2.addDirectors(movie2);
        }
        personDao.save(person1);
        personDao.save(person2);
        personDao.save(person3);
    }

    @Test
    public void findPerson() {
        Iterable<Person> all = personDao.findAll();
        for (Person person : all) {
            System.out.println(person);
        }
    }

    @Test
    public void SupplyRelationship() {
        //采购占比
        String scale = "47.14%";
        // 采购金额
        String amount = "18923.42";
        //添加公司
        Company company = new Company("天猫1");
        //添加供应商
        Supply supply = new Supply("供应商1");
        //添加关系
        SupplyRelationship relationship = new SupplyRelationship();
        relationship.setCompany(company);
        relationship.setAmount(amount);
        relationship.setSupply(supply);
        relationship.setScale(scale);

        //保存
        supplyDao.save(supply);
        companyDao.save(company);
        supplyRelationshipDao.save(relationship);
    }

    @Test
    public void deleteSupplyAndCompany(){
       supplyRelationshipDao.deleteById(23L);
       supplyRelationshipDao.deleteById(46L);
       companyDao.deleteById(58L);
       companyDao.deleteById(60L);
       supplyDao.deleteById(44L);
       supplyDao.deleteById(61L);
    }
    @Test
    public void selectSupplyAndCompany(){
        Iterable<SupplyRelationship> all = supplyRelationshipDao.findAll();
        for (SupplyRelationship supplyRelationship : all) {
            System.out.println(supplyRelationship);
        }
    }
    @Test
    public void selectSupplyAndCompany1(){
        Map[] map=supplyRelationshipDao.find();
        System.out.println(Arrays.toString(map));
    }

//    @Test
//    public void saveUser(){
//        UserNode userNode1 = new UserNode("p1",10);
//        UserNode userNode2 = new UserNode("p2",20);
//        UserNode userNode3 = new UserNode("p3",30);
//        UserNode userNode4 = new UserNode("p4",40);
//        UserRelation userRelation1 = new UserRelation(userNode1,userNode2);
//        UserRelation userRelation2 = new UserRelation(userNode2,userNode3);
//        UserRelation userRelation3 = new UserRelation(userNode3,userNode1);
//        UserRelation userRelation4 = new UserRelation(userNode4,userNode1);
//        userRelation1.setName("userRelation1");
//        userRelation2.setName("userRelation2");
//        userRelation3.setName("userRelation3");
//        userRelation4.setName("userRelation4");
//        userNode1.getUserRelations().add(userRelation1);
//        userNode1.getUserRelations().add(userRelation3);
////        userNode1.getUserRelations().add(userRelation4);
//        userNode2.getUserRelations().add(userRelation1);
//        userNode2.getUserRelations().add(userRelation2);
//        userNode3.getUserRelations().add(userRelation2);
//        userNode3.getUserRelations().add(userRelation3);
//        userNode4.getUserRelations().add(userRelation4);
//        userDao.save(userNode1);
//        userDao.save(userNode2);
//        userDao.save(userNode3);
//        userDao.save(userNode4);
//        userRelationDao.save(userRelation1);
//        userRelationDao.save(userRelation2);
//        userRelationDao.save(userRelation3);
//        userRelationDao.save(userRelation4);
//    }

//    @Test
//    public void saveUser1(){
//        UserNode userNode1 = new UserNode("p1",10);
//        UserNode userNode2 = new UserNode("p2",20);
//        UserNode userNode3 = new UserNode("p3",30);
//        UserRelation userRelation=new UserRelation(userNode1,userNode2);
//        UserRelation userRelation1=new UserRelation(userNode2,userNode3);
//        userNode1.getUserRelations().add(userRelation);
//        userNode2.getUserRelations().add(userRelation1);
//        userDao.save(userNode1);
//        userDao.save(userNode2);
//        userRelationDao.save(userRelation);
//        userRelationDao.save(userRelation1);
//    }

    /**
     * 只保存一方的关系节点
     */
    @Test
    public void saveUser2(){
        UserNode userNode1 = new UserNode("p1",10);
        UserNode userNode2 = new UserNode("p2",20);
        UserNode userNode3 = new UserNode("p3",30);
//        userNode1.getUserNodes().add(userNode2);
//        userNode2.getUserNodes().add(userNode3);
        userDao.save(userNode1);
        userDao.save(userNode2);
        userDao.save(userNode3);
    }

    /**
     * 让中间关系保存双方节点
     */
    @Test
    public void saveUser3(){
        UserNode userNode1 = new UserNode("p1",10);
        UserNode userNode2 = new UserNode("p2",20);
        UserNode userNode3 = new UserNode("p3",30);
        UserNode userNode6 = new UserNode("p6",50);
        UserRelation userRelation1 = new UserRelation(userNode1,userNode2);
        UserRelation userRelation2 = new UserRelation(userNode2,userNode3);
        UserRelation userRelation5 = new UserRelation(userNode6,userNode3);
        Cat cat1 = new Cat("1","公");
        Cat cat2 = new Cat("2","母");
        Cat cat3 = new Cat("3","公");
        Cat cat4 = new Cat("4","母");
        Cat cat5 = new Cat("5","母");
        Cat cat6 = new Cat("6","母");
        //string
        userRelation1.getList().add(JSON.toJSONString(cat1));
        userRelation1.getList().add(JSON.toJSONString(cat2));
        userRelation1.setName("12");
        userRelation2.getList().add(JSON.toJSONString(cat3));
        userRelation2.getList().add(JSON.toJSONString(cat4));
        userRelation2.setName("23");
        userRelation5.getList().add(JSON.toJSONString(cat5));
        userRelation5.getList().add(JSON.toJSONString(cat6));
        userRelation5.setName("63");
        //object
//        userRelation1.getList().add(cat1);
//        userRelation1.getList().add(cat2);
//        userRelation1.setName("12");
//        userRelation2.getList().add(cat3);
//        userRelation2.getList().add(cat4);
//        userRelation2.setName("23");
//        userRelation5.getList().add(cat5);
//        userRelation5.getList().add(cat6);
//        userRelation5.setName("63");
        //array
//        userRelation1.getArray()[0]=cat1;
//        userRelation1.getArray()[1]=cat2;
//        userRelation1.setName("12");
//        userRelation2.getArray()[0]=cat3;
//        userRelation2.getArray()[0]=cat4;
//        userRelation2.setName("23");
//        userRelation5.getArray()[0]=cat5;
//        userRelation5.getArray()[0]=cat6;
//        userRelation5.setName("63");
        userNode1.setUserRelation(userRelation1);
        userNode2.setUserRelation(userRelation2);
        userNode3.setUserRelation(userRelation5);
        userNode6.setUserRelation(userRelation5);
        userDao.save(userNode1);
        userDao.save(userNode2);
        userDao.save(userNode3);
        userDao.save(userNode6);
//        userRelationDao.save(userRelation1);
//        userRelationDao.save(userRelation2);
//        userRelationDao.save(userRelation5);

    }

    @Test
    public void findUser_aaa(){
        List<UserNodeAndUserRelationship> userNodeList3 = userDao.getUserNodeList3(33L);
        for (UserNodeAndUserRelationship relationship : userNodeList3) {

        }
    }

    @Test
    public void findUser(){
        List<Map<String,Object>[]> userNodeList0 = userDao.getUserNodeList0();
        List<UserNodeAndUserRelationship> userNodeList1 = userDao.getUserNodeList2();
        for (Map<String, Object>[] maps : userNodeList0) {
            for (Map<String, Object> map : maps) {
                System.out.println(map);
            }
        }
        //============================================================
        for (UserNodeAndUserRelationship userNodeAndUserRelationship : userNodeList1) {
            System.out.println(userNodeAndUserRelationship);
        }
    }
    @Test
    public void findUserByName(){
//        UserNode userNode1 = userDao.findByName("p1").get();
        UserNode userNode2 = userDao.findByName("p2").get();
//        UserNode userNode3 = userDao.findByName("p3").get();
//        System.out.println(userNode1);
        System.out.println(userNode2);
//        System.out.println(userNode3);
    }
    @Test
    public void findUserRelation(){
        Iterable<UserRelation> all =
                userRelationDao.findAll();
        all.forEach(item-> System.out.println(item));
    }

    @Test
    public void findAllUser(){
        Iterable<UserNode> all = userDao.findAll();
        System.out.println("");
    }
    @Test
    public void deleteUser(){
        userDao.deleteAll();
        userRelationDao.deleteAll();
        catDao.deleteAll();
    }

    @Test
    public void saveShop(){
        Shop shop1 = new Shop("店铺1");
        Shop shop2 = new Shop("店铺2");
        shop1.getShops().add(shop2);
        shop2.getShops().add(shop1);
        shopDao.save(shop1);
        shopDao.save(shop2);
    }
    @Test
    public void findShop(){
        Iterable<Shop> all = shopDao.findAll();
        for (Shop shop : all) {
            System.out.println(shop);
        }
    }

    @Test
    public void saveMan(){
        Man man1 = new Man("爷爷");
        Man man20 = new Man("爸爸1");
        Man man21 = new Man("爸爸2");
        Man man30 = new Man("儿子11");
        Man man31 = new Man("儿子12");
        Man man32 = new Man("儿子21");
        Man man33 = new Man("儿子22");

        ParentShip parentShip1 = new ParentShip();
        parentShip1.setParent(man1);
        parentShip1.setChild(man20);

        ParentShip parentShip2 = new ParentShip();
        parentShip2.setParent(man1);
        parentShip2.setChild(man21);

        //爷爷有2个父子关系
        man1.getParentShips().add(parentShip1);
        man1.getParentShips().add(parentShip2);

        man20.getParentShips().add(parentShip1);
        man21.getParentShips().add(parentShip2);

        ParentShip parentShip3 = new ParentShip();
        parentShip3.setParent(man20);
        parentShip3.setChild(man30);

        ParentShip parentShip4 = new ParentShip();
        parentShip4.setParent(man20);
        parentShip4.setChild(man31);

        man20.getParentShips().add(parentShip3);
        man20.getParentShips().add(parentShip4);

        man30.getParentShips().add(parentShip3);
        man31.getParentShips().add(parentShip4);

        ParentShip parentShip5 = new ParentShip();
        parentShip5.setParent(man21);
        parentShip5.setChild(man32);

        ParentShip parentShip6 = new ParentShip();
        parentShip6.setParent(man21);
        parentShip6.setChild(man33);

        man21.getParentShips().add(parentShip5);
        man21.getParentShips().add(parentShip6);

        man32.getParentShips().add(parentShip5);
        man33.getParentShips().add(parentShip6);

        manDao.save(man1);
        manDao.save(man20);
        manDao.save(man21);
        manDao.save(man30);
        manDao.save(man31);
        manDao.save(man32);
        manDao.save(man33);
        parentShipDao.save(parentShip1);
        parentShipDao.save(parentShip2);
        parentShipDao.save(parentShip3);
        parentShipDao.save(parentShip4);
        parentShipDao.save(parentShip5);
        parentShipDao.save(parentShip6);

    }

    @Test
    public void test11(){
        long a=9;
        long b=10;
        double c=a/b;
        System.out.println(a/b);
    }
}
