package com.ot.springboot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JdbcNeo4j {

    public static void main(String[] args) {
//        String url="bolt://localhost:7687";
        String url="jdbc:neo4j:bolt://localhost:7687";
        String driverName="";
        String username="neo4j";
        String password="root";
        try {
            Class.forName("org.neo4j.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            String cql=" MATCH  (p:User)  RETURN  p  ";
            PreparedStatement ps = connection.prepareStatement(cql);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs.getRow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
