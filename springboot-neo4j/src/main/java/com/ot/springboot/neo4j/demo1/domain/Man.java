package com.ot.springboot.neo4j.demo1.domain;

import com.ot.springboot.neo4j.demo1.relationship.ParentShip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NodeEntity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Man {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @Relationship(type = "fuzi")
    Set<ParentShip> parentShips=new HashSet<>();

    public Man(@NonNull String name) {
        this.name = name;
    }
}
