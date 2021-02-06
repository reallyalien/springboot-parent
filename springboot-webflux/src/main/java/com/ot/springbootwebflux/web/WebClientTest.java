package com.ot.springbootwebflux.web;

import com.ot.springbootwebflux.entity.MyEvent;
import com.ot.springbootwebflux.entity.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 比如：Accept：text/xml（application/json）;
 * 代表客户端希望接受的数据类型是xml（json ）类型
 * Content-Type代表发送端（客户端|服务器）发送的实体数据的数据类型。
 * <p>
 * 比如：Content-Type：text/html（application/json） ;
 * 代表发送端发送的数据格式是html（json）。
 * 二者合起来，
 * <p>
 * Accept:text/xml；
 * Content-Type:text/html
 * <p>
 * 即代表希望接受的数据类型是xml格式，本次请求发送的数据的数据格式是html。
 */

public class WebClientTest {

    @Test
    public void webClientTest1() throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080/");
        Mono<String> resp = client.get().uri("/time").retrieve().bodyToMono(String.class);
        resp.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void webClientTest2() throws InterruptedException {
        WebClient client = WebClient.create("http://localhost:8080/");
        client.get().uri("/user/findAll")
                .accept(MediaType.APPLICATION_STREAM_JSON)//代表客户端想要接收到的数据类型
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(User.class))
                .doOnNext(System.out::println)//只读的peek每个元素，然后打印出来，它并不是subscribe，所以不会触发流
                .blockLast();//在收到最后一个元素之前会阻塞，响应式业务场景慎用

        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void webClientTest3() {
        WebClient client = WebClient.create("http://localhost:8080");
        client.get().uri("/sec")
                .accept(MediaType.TEXT_EVENT_STREAM) //配置请求的accept，即客户端想要获取的数据类型
                /*
                    SSE是websocket的一种轻型替代方案。
                    和websocket有以下几点不同：

                    SSE是使用http协议，而websocket是一种单独的协议
                    SSE是单向传输，只能服务端向客户端推送，websocket是双向
                    SSE支持断点续传，websocket需要自己实现
                    SSE支持发送自定义类型消息
                 */
                .retrieve()
                .bodyToFlux(String.class)
                .log()//这次用`log()`代替`doOnNext(System.out::println)`来查看每个元素；
                .take(10)//由于sec是个无限流，这里取前10个，会导致流被取消
                .blockLast();
    }

    @Test
    public void webClientTest4() {
        //声明一个数据流，不加take表示无限的数据流
        Flux<MyEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new MyEvent(System.currentTimeMillis(), "message-" + l))
                .take(5);
        WebClient client = WebClient.builder().baseUrl("http://localhost:8080/event").build();
        client.post()
                .uri("/loadEvents")
                .contentType(MediaType.APPLICATION_STREAM_JSON)//声明请求体的content-type
                .body(eventFlux, MyEvent.class)//body方法设置请求体
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
