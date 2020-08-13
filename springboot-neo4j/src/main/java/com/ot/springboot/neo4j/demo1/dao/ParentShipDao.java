package com.ot.springboot.neo4j.demo1.dao;

import com.ot.springboot.neo4j.demo1.relationship.ParentShip;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

public interface ParentShipDao  extends Neo4jRepository<ParentShip,Long> {
}
