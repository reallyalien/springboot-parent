package com.ot.springboot.neo4j;

import com.ot.springboot.neo4j.dao.MovieDao;
import com.ot.springboot.neo4j.dao.PersonDao;
import com.ot.springboot.neo4j.dao.UserDao;
import com.ot.springboot.neo4j.domain.Movie;
import com.ot.springboot.neo4j.domain.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Neo4jTest {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private MovieDao movieDao;
    @Autowired
    private UserDao userDao;

    @Test
    public void saveMovie(){
        Movie movie1 = new Movie("无问东西", "2018");
        Movie movie2 = new Movie("罗曼蒂克消亡史", "2016");
        movieDao.save(movie1);
        movieDao.save(movie2);
    }

    @Test
    public void findMovie(){
        Iterable<Movie> all = movieDao.findAll();
        for (Movie movie : all) {
            System.out.println(movie);
        }
    }

    @Test
    public void savePerson(){
        Person person1 = new Person("章子怡", "1979");
        Person person2 = new Person("李芳芳", "1976");
        Person person3 = new Person("程耳", "1979");
        Movie movie1 = movieDao.findByTitle("罗曼蒂克消亡史");
        Movie movie2 = movieDao.findByTitle("无问东西");
        if (movie1 != null) {
            person1.addActor(movie1);
            person3.addDirectors(movie1);
        }
        if (movie2!=null){
            person1.addActor(movie2);
            person2.addDirectors(movie2);
        }
        personDao.save(person1);
        personDao.save(person2);
        personDao.save(person3);
    }

    @Test
    public void findPerson(){
        Iterable<Person> all = personDao.findAll();
        for (Person person : all) {
            System.out.println(person);
        }
    }
}
