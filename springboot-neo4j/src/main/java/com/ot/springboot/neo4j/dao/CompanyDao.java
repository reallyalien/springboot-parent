package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Company;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface CompanyDao extends Neo4jRepository<Company,Long> {
}
