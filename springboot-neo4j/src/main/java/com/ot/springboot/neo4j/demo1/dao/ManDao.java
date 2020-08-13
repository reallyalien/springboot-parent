package com.ot.springboot.neo4j.demo1.dao;

import com.ot.springboot.neo4j.demo1.domain.Man;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;


public interface ManDao extends Neo4jRepository<Man,Long> {
}
