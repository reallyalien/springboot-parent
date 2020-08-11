package com.ot.springboot.neo4j.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.ot.springboot.neo4j.dao")
public class Neo4jConfig {
}
