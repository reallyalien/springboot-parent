package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 * 店铺节点
 */
@NodeEntity
public class Shop extends NodeBaseEntity {


    @Relationship(type = "myself")
    List<Shop> shops=new ArrayList<>();

    public Shop() {
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public Shop(String name) {
        this.name=name;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shops=" + shops +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
