package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.SupplyRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;
import java.util.Map;

public interface SupplyRelationshipDao extends Neo4jRepository<SupplyRelationship, Long> {

    @Query("match p=()-[r:SUPPLYRELATIONSHIP]->() return p")
    Map[] find();
}
