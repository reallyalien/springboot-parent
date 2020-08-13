package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.UserRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRelationDao extends Neo4jRepository<UserRelation,Long> {


    @Query("match p= (n:User)<- [r: UserRelation]-> (n1:User) wheren.userId=(firstUserId) and n1.userId =(secondUserId) return p")
    List<UserRelation> findUserRelationByEachId (@Param(" firstUserId") String firstUserId, @Param ("secondUserId") String secondUserId) ;

    @Query ("match(fu:User) ,(su:User) where fu. userId=[firstUserId]and su.userId=[ secondUserId, create p= (fu)- [r:UserRelation]-> (su) return p")

    List<UserRelation> addUserRelation (@Param(" firstUserId") String firstUserId, @Param("secondUserId") String secondUserId) ;
}
