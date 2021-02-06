package com.ot.springbootwebflux.service;

import com.ot.springbootwebflux.entity.User;
import com.ot.springbootwebflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 保存或更新。
     * 如果传入的user没有id属性，由于username是unique的，在重复的情况下有可能报错，
     * 这时找到已保存的user记录用传入的user更新（覆盖）它。
     *
     * @param user
     * @return
     */
    public Mono<User> save(User user) {
        Date date = new Date();
        user.setBirthday(date);
        return userRepository
                .save(user)
                .onErrorResume(e ->
                        userRepository
                                .findByUsername(user.getUsername())
                                .flatMap(originalUser -> {
                                    user.setId(originalUser.getId());
                                    return userRepository.save(user);
                                })
                );
    }

    public Mono<Long> deleteByUsername(String username) {
        return userRepository.deleteByUsername(username);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Mono<Void> deleteById(String id){
        return userRepository.deleteById(id);
    }
}
