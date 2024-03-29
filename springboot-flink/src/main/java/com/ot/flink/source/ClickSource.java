package com.ot.flink.source;

import com.ot.flink.entity.Event;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * 持续发送数据
 */
public class ClickSource implements SourceFunction<Event> {

    private volatile boolean running = true;

    @Override
    public void run(SourceContext<Event> sourceContext) throws Exception {
        Random r = new Random();
//        String[] users = {"G", "H", "I", "J", "A", "B", "C", "D", "E", "F"};
        String[] users = {"A", "B", "C", "D", "E"};
        String[] urls = {"./home", "./cart", "./fav", "./prod?id=1", "./prod?id=2"};
//        while (running) {
        for (int i = 0; i < 50; i++) {
            Event build = Event.builder()
                    .user(users[r.nextInt(users.length)])
                    .url(urls[r.nextInt(urls.length)])
                    .timestamp(System.currentTimeMillis())
                    .build();
            sourceContext.collect(
                    build
            );
//            System.out.println("1-->" + build);
            Thread.sleep(1000);
        }
//        Thread.sleep(10000);
    }
//    }

    @Override
    public void cancel() {
        running = false;
    }
}
