package com.ot.springboot.neo4j.domain;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Map;

@Data
@QueryResult
public class UserNodeAndUserRelationship {

    private UserNode startNode;

    private Long id;

    private UserNode endNode;


}
