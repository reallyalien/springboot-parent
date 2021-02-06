package com.ot.springbootwebflux.repository;

import com.ot.springbootwebflux.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {


    Mono<User> findByUsername(String username);     // 2

    Mono<Long> deleteByUsername(String username);
}
