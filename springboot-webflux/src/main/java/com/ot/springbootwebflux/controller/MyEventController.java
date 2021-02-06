package com.ot.springbootwebflux.controller;

import com.ot.springbootwebflux.entity.MyEvent;
import com.ot.springbootwebflux.repository.MyEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/event")
public class MyEventController {

    @Autowired
    private MyEventRepository myEventRepository;

    /**
     * 指定传入的参数式`application/stream+json`
     * insert返回的是保存成功的flux的，但是我们不需要，使用then来忽略数据元素，只返回一个完成信号
     * @param events
     * @return
     */
    @PostMapping(path = "/loadEvents",consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Void> loadEvents(@RequestBody Flux<MyEvent> events) {
        return myEventRepository.insert(events).then();
    }

    @GetMapping(path = "/getEvents", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<MyEvent> getEvents() {  // 2
        return myEventRepository.findBy();
    }
}
