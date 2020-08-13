package com.ot.springboot.neo4j.domain;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@Data
@QueryResult
public class UserNodeAndUserRelationship {

    private UserNode startNode;

    private UserRelation userRelation;

    private UserNode endNode;

    @Override
    public String toString() {
        return "UserNodeAndUserRelationship{" +
                "startNode=" + startNode +
                ", userRelation=" + userRelation +
                ", endNode=" + endNode +
                '}';
    }
}
