package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Movie;
import org.springframework.data.repository.CrudRepository;

public interface MovieDao extends CrudRepository<Movie, Long> {
    Movie findByTitle(String title);
}
