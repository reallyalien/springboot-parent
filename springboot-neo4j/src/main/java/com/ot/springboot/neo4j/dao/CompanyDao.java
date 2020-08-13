package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Company;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import javax.xml.transform.sax.SAXTransformerFactory;

public interface CompanyDao extends Neo4jRepository<Company,Long> {
    String findByName(String name, @Depth int depth);
}
