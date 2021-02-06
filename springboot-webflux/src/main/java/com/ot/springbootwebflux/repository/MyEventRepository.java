package com.ot.springbootwebflux.repository;

import com.ot.springbootwebflux.entity.MyEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface MyEventRepository extends ReactiveMongoRepository<MyEvent, Long> {

    /**
     * 1. `@Tailable`注解的作用类似于linux的`tail`命令，被注解的方法将发送无限流，需要注解在返回值为Flux这样的多个元素
     *     的Publisher的方法上；
     * 2. `findAll()`是想要的方法，但是在`ReactiveMongoRepository`中我们够不着，所以使用`findBy()`代替。
     *
     *3. 此注解仅支持有大小限制的collection，而自动创建的collection是不受大小限制的，因此需要手动创建，spring boot提供的CommandLineRunner
     *   可以帮我们实现
     *
     *   注解起作用的前提是至少要有一条记录
     * @return
     */
    @Tailable
    Flux<MyEvent> findBy();
}
