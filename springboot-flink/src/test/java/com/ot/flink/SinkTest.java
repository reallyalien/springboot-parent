package com.ot.flink;

import com.ot.flink.entity.Event;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SinkTest {

    StreamExecutionEnvironment env = null;

    @Before
    public void before() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @After
    public void after() throws Exception {
        env.execute();
    }

    @Test
    public void file() {
        env.setParallelism(4);
        DataStreamSource<Event> streamSource = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L),
                new Event("Alice", "./prod?id=100", 3000L),
                new Event("Alice", "./prod?id=200", 3500L),
                new Event("Bob", "./prod?id=2", 2500L),
                new Event("Alice", "./prod?id=300", 3600L),
                new Event("Bob", "./home", 3000L),
                new Event("Bob", "./prod?id=1", 2300L),
                new Event("Bob", "./prod?id=3", 3300L)
        );
        StreamingFileSink<String> fileSink = StreamingFileSink
                .<String>forRowFormat(new Path("./output"), new SimpleStringEncoder<>("UTF-8"))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(15))//至少包含15分钟数据
                                .withInactivityInterval(TimeUnit.MINUTES.toMillis(5))//最近5分钟没有收到新数据
                                .withMaxPartSize(1024 * 1024 * 1024)//文件大小已达到1GB
                                .build()
                )
                .build();
        streamSource.map(Event::toString).addSink(fileSink);
    }


    @Test
    public void kafka() {
        env.setParallelism(4);
        DataStreamSource<Event> streamSource = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L),
                new Event("Alice", "./prod?id=100", 3000L),
                new Event("Alice", "./prod?id=200", 3500L),
                new Event("Bob", "./prod?id=2", 2500L),
                new Event("Alice", "./prod?id=300", 3600L),
                new Event("Bob", "./home", 3000L),
                new Event("Bob", "./prod?id=1", 2300L),
                new Event("Bob", "./prod?id=3", 3300L)
        );
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "196.128.100.1");
        FlinkKafkaProducer<Event> kafkaProducer = new FlinkKafkaProducer<Event>("", new SerializationSchema<Event>() {
            @Override
            public byte[] serialize(Event element) {
                byte[] bytes = element.toString().getBytes();
                return bytes;
            }
        }, properties);
        streamSource.addSink(kafkaProducer);
    }
}
