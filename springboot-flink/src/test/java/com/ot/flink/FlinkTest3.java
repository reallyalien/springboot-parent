package com.ot.flink;


import com.ot.flink.entity.Event;
import com.ot.flink.function.MyFlatMap;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.junit.Test;

public class FlinkTest3 {

    /**
     * 我们将数据流按照用户 id 进行分区，然后用一个 reduce 算子实现 sum 的功能，统计每个
     * 用户访问的频次；进而将所有统计结果分到一组，用另一个 reduce 算子实现 maxBy 的功能，
     * 记录所有用户中访问频次最高的那个，也就是当前访问量最大的用户是谁
     *
     * @throws Exception
     */
    @Test
    public void reduce() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .map(new MapFunction<Event, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(Event value) throws Exception {
                        return Tuple2.of(value.user, 1L);
                    }
                })
                .keyBy(e -> e.f0)
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                })
                .keyBy(r -> true)//所有数据分成n组
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                        return value1.f1 > value2.f1 ? value1 : value2;
                    }
                })
                .print();
        env.execute();
    }

    @Test
    public void map() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> dataStreamSource = env.fromElements("1", "a", "c", "d");
        dataStreamSource.map(String::toUpperCase).print();
        env.execute();
    }

    @Test
    public void richMap() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        DataStreamSource<String> dataStreamSource = env.fromElements("1", "a", "c", "d");
        dataStreamSource.map(new RichMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(String value) throws Exception {
                return Tuple2.of(value, 1L);
            }

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                System.out.println("索引为" + getRuntimeContext().getIndexOfThisSubtask() + "任务开始了");
            }

            @Override
            public void close() throws Exception {
                super.close();
                System.out.println("索引为" + getRuntimeContext().getIndexOfThisSubtask() + "任务结束了");
            }
        }).print();
        env.execute();
    }

    @Test
    public void richMap2() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);
        DataStreamSource<String> dataStreamSource = env.fromElements("1", "a", "c", "d");
        dataStreamSource.flatMap(new MyFlatMap()).print();
        env.execute();
    }
}
