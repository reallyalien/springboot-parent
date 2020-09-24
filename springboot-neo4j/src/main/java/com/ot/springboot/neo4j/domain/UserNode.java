package com.ot.springboot.neo4j.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.cypher.internal.frontend.v2_3.symbols.RelationshipType;
import org.neo4j.ogm.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Property(name = "date")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
        @Relationship(type = "UserRelation",direction = Relationship.UNDIRECTED)
    private List<UserNode> userNodes=new ArrayList<>(10);

    /**
     * 用实体类来保存关系，建议设置关系为无向的，这样a->b->c在这条关系链当中，a,c分别有一条关系，b有2条关系
     */
    @Relationship(type = "UserRelation1",direction =Relationship.UNDIRECTED)//
//    private List<UserRelation> userRelations=new ArrayList<>(10);
    private UserRelation userRelation;

    public UserNode() {
    }

    public UserNode(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
