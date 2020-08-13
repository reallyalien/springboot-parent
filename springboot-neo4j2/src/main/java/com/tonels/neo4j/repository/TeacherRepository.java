package com.tonels.neo4j.repository;

import com.tonels.neo4j.model.Teacher;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TeacherRepository extends Neo4jRepository<Teacher, String> {
}
