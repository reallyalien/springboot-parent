package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.SupplyRelationship;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SupplyRelationshipDao extends Neo4jRepository<SupplyRelationship, Long> {
}
