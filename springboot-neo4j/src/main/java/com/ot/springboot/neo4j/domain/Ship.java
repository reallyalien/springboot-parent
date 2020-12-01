package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.RelationshipEntity;

@RelationshipEntity(type = "myself")
public class Ship<Start,End> {

    private Start start;

    private End end;

    public static void main(String[] args) {
        Ship<Company, Supply> ship = new Ship<>();
    }
}
