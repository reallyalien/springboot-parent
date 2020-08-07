package com.ot.springbootwebflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/mono")
    public Mono<String> mono(){
        return Mono.just("hello webflux");
    }
}
