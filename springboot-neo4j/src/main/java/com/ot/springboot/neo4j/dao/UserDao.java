package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.UserNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Neo4jRepository--->PagingAndSortingRepository-->CrudRepository-->Repository
 */
public interface UserDao extends Neo4jRepository<UserNode, Long> {

    @Query("match (n:User) return n")
    List<UserNode> getUserNodeList();

    @Query("create (n:User{age:{age},name:{name}}) RETURN n ")
    List<UserNode> addUserNodeList(@Param("name") String name, @Param("age") int age);
}
