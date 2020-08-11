package com.ot.springboot.neo4j.dao;

import com.ot.springboot.neo4j.domain.Person;
import org.springframework.data.repository.CrudRepository;


public interface PersonDao extends CrudRepository<Person,Long>{
    Person findByName(String name);
}
