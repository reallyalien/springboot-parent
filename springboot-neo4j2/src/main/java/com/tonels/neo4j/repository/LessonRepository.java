package com.tonels.neo4j.repository;

import com.tonels.neo4j.model.Lesson;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface LessonRepository extends Neo4jRepository<Lesson, String> {
}
