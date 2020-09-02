package com.ot.springboot.neo4j.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.cypher.internal.frontend.v2_3.symbols.RelationshipType;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.*;

@Data
@NodeEntity(label = "User")
public class UserNode implements Serializable {

    @Id //主键id，必须是Long
    @GeneratedValue
    private Long nodeId;

    @Property(name = "name")
    private String name;

    @Property(name = "age")
    private int age;

//    @Relationship(type = "UserRelation",direction = Relationship.UNDIRECTED)
//    private List<UserNode> userNodes=new ArrayList<>(10);

    /**
     * 用实体类来保存关系，建议设置关系为无向的，这样a->b->c在这条关系链当中，a,c分别有一条关系，b有2条关系
     */
    @Relationship(type = "UserRelation",direction =Relationship.UNDIRECTED)//
//    private List<UserRelation> userRelations=new ArrayList<>(10);
    private UserRelation userRelation;

    public UserNode() {
    }

    public UserNode(String name, int age) {
        this.name = name;
        this.age = age;
    }

//    public Long getNodeId() {
//        return nodeId;
//    }
//
//    public void setNodeId(Long nodeId) {
//        this.nodeId = nodeId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public List<UserNode> getUserNodes() {
//        return userNodes;
//    }
//
//    public void setUserNodes(List<UserNode> userNodes) {
//        this.userNodes = userNodes;
//    }
//
//    public List<UserRelation> getUserRelations() {
//        return userRelations;
//    }
//
//    public void setUserRelations(List<UserRelation> userRelations) {
//        this.userRelations = userRelations;
//    }
}
