package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "UserRelation")
public class UserRelation {
    @Id
    @GeneratedValue
    private Long id;
    //开始节点
    @StartNode
    private UserNode startNode;
    //结束节点
    @EndNode
    private UserNode endNode;

    private String name;
    public UserRelation() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserRelation(UserNode startNode, UserNode endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    @Override
    public String toString() {
        return "UserRelation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
