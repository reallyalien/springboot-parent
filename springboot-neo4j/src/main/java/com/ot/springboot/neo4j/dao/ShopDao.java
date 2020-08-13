package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Shop;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ShopDao extends Neo4jRepository<Shop,Long> {

}
