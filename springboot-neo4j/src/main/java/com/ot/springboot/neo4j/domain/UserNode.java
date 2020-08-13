package com.ot.springboot.neo4j.domain;

import org.neo4j.cypher.internal.frontend.v2_3.symbols.RelationshipType;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity(label = "User")
public class UserNode {

    @Id //主键id，必须是Long
    @GeneratedValue
    private Long nodeId;
//
//    @Property(name = "userId")//节点属性值，支持8种基本类型和String
//    private String userId;

    @Property(name = "name")
    private String name;

    @Property(name = "age")
    private int age;

//    @Relationship(type = "UserRelation",direction =Relationship.UNDIRECTED)//
    private Set<UserRelation> userRelations=new HashSet<>();

    public UserNode() {
    }

    public Set<UserRelation> getUserRelations() {
        return userRelations;
    }

    public void setUserRelations(Set<UserRelation> userRelations) {
        this.userRelations = userRelations;
    }

    public UserNode(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserNode{" +
                "nodeId=" + nodeId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", userRelations=" + userRelations +
                '}';
    }
}
