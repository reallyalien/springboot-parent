package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

/**
 * neo4j的实体类 OGM 对象图形映射
 */
//@NodeEntity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String born;

    /**
     * 建立movie与演员，导演之间的指向关系,type是关系的名称，direction是关系的方向
     */
    @Relationship(type = "ACTED_IN", direction = Relationship.OUTGOING)
    public Set<Movie> actors = new HashSet<>();

    public void addActor(Movie movie) {
        if (movie != null) {
            actors.add(movie);
        }
    }

    @Relationship(type = "DIRECTED", direction = Relationship.OUTGOING)
    public Set<Movie> directors = new HashSet<>();

    public void addDirectors(Movie movie) {
        if (movie != null) {
            directors.add(movie);
        }
    }



    //=================================================================================================

    public Person() {//无参构造在2.5之后必须要有
    }

    public Person(String name, String born) {
        this.name = name;
        this.born = born;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", born='" + born + '\'' +
                ", actors=" + actors +
                ", directors=" + directors +
                '}';
    }
}
