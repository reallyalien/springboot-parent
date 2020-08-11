package com.ot.springboot.neo4j.demo;

import org.neo4j.cypher.internal.javacompat.ExecutionEngine;
import org.neo4j.cypher.internal.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4jCqlDemo2 {
    public static void main(String[] args) {
        GraphDatabaseFactory factory = new GraphDatabaseFactory();
        GraphDatabaseService db = factory.
                newEmbeddedDatabase(new File("E:\\develop\\Neo4j\\neo4j-community-3.5.0-windows\\data\\databases\\test.db"));
//        ExecutionEngine execEngine = new ExecutionEngine(db);
//        ExecutionResult execResult = execEngine.execute("MATCH (java:JAVA) RETURN java");
//        String results = execResult.dumpToString();
//        System.out.println(results);

    }
}
