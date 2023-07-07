package com.ot.flink;

import com.ot.flink.entity.Event;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.After;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

public class BaseTest {

    public List<Event> eventList = Arrays.asList(new Event("Mary", "./home", 1000L),
            new Event("Bob", "./cart", 2000L),
            new Event("Alice", "./prod?id=100", 3000L),
            new Event("Alice", "./prod?id=200", 3500L),
            new Event("Bob", "./prod?id=2", 2500L),
            new Event("Alice", "./prod?id=300", 3600L),
            new Event("Bob", "./home", 3000L),
            new Event("Bob", "./prod?id=1", 2300L),
            new Event("Bob", "./prod?id=3", 3300L));

    public StreamExecutionEnvironment   env = null;

    @Before
    public void before() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @After
    public void after() throws Exception {
        env.execute("aaaa");
    }
}
