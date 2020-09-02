package com.ot.springboot.neo4j.domain;

import com.sun.source.doctree.SerialDataTree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Data
@RelationshipEntity(type = "UserRelation")
public class UserRelation implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private Boolean flag=false;
    @Property
    private Double d=1.2;
    /**
     * 只能放String，map，对象都不行
     */
    @Property
    private List<String> list=new ArrayList<>(10);



//    @Property
//    private Cat[] array=new Cat[2];
//
//    public Cat[] getArray() {
//        return array;
//    }

//    public void setArray(Cat[] array) {
//        this.array = array;
//    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    public UserRelation() {

    }

    //开始节点
    @StartNode
    private UserNode startNode;
    //结束节点
    @EndNode
    private UserNode endNode;
    public UserRelation(UserNode startNode, UserNode endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public UserNode getStartNode() {
//        return startNode;
//    }

    public void setStartNode(UserNode startNode) {
        this.startNode = startNode;
    }

//    public UserNode getEndNode() {
//        return endNode;
//    }

    public void setEndNode(UserNode endNode) {
        this.endNode = endNode;
    }
}
