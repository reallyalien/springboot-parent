package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.UserNode;
import com.ot.springboot.neo4j.domain.UserNodeAndUserRelationship;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Neo4jRepository--->PagingAndSortingRepository-->CrudRepository-->Repository
 */
public interface UserDao extends Neo4jRepository<UserNode, Long> {

    @Query("match p=(n:User)-[r:UserRelation]->(n1:User) return p")
    List<Map<String,Object>[]> getUserNodeList0();


    @Query("match (n:User)-[r:UserRelation]->(n1:User) with n as startNode,r as userRelation,n1 as endNode return startNode,userRelation,endNode")
    List<UserNodeAndUserRelationship> getUserNodeList1();

    @Query("match (n:User)-[r:UserRelation]->(n1:User) with n as startNode,r.id as id,n1 as endNode return startNode,id,endNode")
    List<UserNodeAndUserRelationship> getUserNodeList2();

    @Query("create (n:User{age:{age},name:{name}}) RETURN n ")
    List<UserNode> addUserNodeList(@Param("name") String name, @Param("age") int age);

    Optional<UserNode> findByName(String name);

    @Query("match (n:User)-[r:UserRelation*1..10]->(n1:User) where id(n)={id} or id(n1)={id} with n as startNode,r as userRelation,n1 as endNode return startNode,userRelation,endNode")
    List<UserNodeAndUserRelationship> getUserNodeList3(@Param("id") Long id);
    @Query(" match (p) return distinct labels(p) ")
    List<String> getNodeLabel();

    @Query(" match ()-[r]->() return distinct type(r) ")
    List<String> getRelationType();


    @Query(" match (p:label) return keys(p) ")
    List<String[]> getNodeAttribute(@Param("label") String label);
}
