package com.ot.springbootwebflux.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TimeHandler {

    public Mono<ServerResponse> getTime(ServerRequest request) {
        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());
        return ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just("now is " + now), String.class);
    }

    public Mono<ServerResponse> getDate(ServerRequest request) {
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(Mono.just("today is " + now), String.class);
    }

    /**
     * 向服务器端推送消息
     * TEXT_EVENT_STREAM
     * 即SSE：服务端推送（Server Send Event），在客户端发起一次请求后会保持该连接，服务器端基于该连接持续向客户端发送数据，
     * 从HTML5开始加入。
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(Flux
                        .interval(Duration.ofSeconds(1))
                        .map(l -> format.format(new Date())), String.class).log();
    }
}
