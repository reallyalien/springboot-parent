package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "User")
public class UserNode {

    @Id //主键id，必须是Long
    private Long nodeId;

    @Property(name = "userId")//节点属性值，支持8种基本类型和String
    private String userId;

    @Property(name = "name")
    private String name;

    @Property(name = "age")
    private int age;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
}
