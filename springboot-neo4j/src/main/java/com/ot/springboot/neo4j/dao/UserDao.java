package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.UserNode;
import com.ot.springboot.neo4j.domain.UserNodeAndUserRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

/**
 * Neo4jRepository--->PagingAndSortingRepository-->CrudRepository-->Repository
 */
public interface UserDao extends Neo4jRepository<UserNode, Long> {

    @Query("match p=(n:User)-[r:UserRelation]->(n1:User) return p")
    List<Map<String,Object>[]> getUserNodeList0();


    @Query("match (n:User)-[r:UserRelation]->(n1:User) with n as startNode,r as userRelation,n1 as endNode return startNode,userRelation,endNode")
    List<UserNodeAndUserRelationship> getUserNodeList1();

    @Query("create (n:User{age:{age},name:{name}}) RETURN n ")
    List<UserNode> addUserNodeList(@Param("name") String name, @Param("age") int age);
}
