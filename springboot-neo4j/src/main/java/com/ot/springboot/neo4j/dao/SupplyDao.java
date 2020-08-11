package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Supply;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface SupplyDao extends Neo4jRepository<Supply,Long> {

}
