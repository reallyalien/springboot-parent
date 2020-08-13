package com.ot.springboot.neo4j.demo1.relationship;

import com.ot.springboot.neo4j.demo1.domain.Man;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "fuzi")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParentShip {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Man parent;

    @EndNode
    private Man child;
}
