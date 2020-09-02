package com.ot.springboot.neo4j.controller;

import com.ot.springboot.neo4j.dao.UserDao;
import com.ot.springboot.neo4j.domain.UserNode;
import com.ot.springboot.neo4j.domain.UserRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userNode")
public class UserNodeController {

    @Autowired
    private UserDao userDao;

//    @GetMapping("/findByName/{name}")
//    public UserNode findById(@PathVariable("name")String name){
//        UserNode userNode = userDao.findByName(name).get();
//        List<UserRelation> userRelations = userNode.getUserRelations();
//        return userNode;
//    }
}
