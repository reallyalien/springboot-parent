package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Cat;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CatDao extends Neo4jRepository<Cat,Long> {

}
