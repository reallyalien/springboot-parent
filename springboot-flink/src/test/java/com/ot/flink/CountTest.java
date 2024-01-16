package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.junit.Test;

import java.util.*;

/**
 * 时间窗口测试
 */
public class CountTest extends BaseTest {


    /**
     * 水位线
     */
    @Test
    public void watermark() {
        List<Tuple2<String, Integer>> list = new ArrayList<>();
        list.add(Tuple2.of("A", 1));
        list.add(Tuple2.of("A", 3));
        list.add(Tuple2.of("A", 2));
        DataStreamSource<Tuple2<String, Integer>> streamSource = env.fromCollection(list);
        KeyedStream<Tuple2<String, Integer>, String> keyBy = streamSource.keyBy(e -> e.f0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> reduce = keyBy.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
            }
        });
        reduce.print();
    }

    /**
     * 水位线
     */
    @Test
    public void watermark2() {
        List<Tuple2<String, Integer>> list = new ArrayList<>();
        list.add(Tuple2.of("A", 1));
        list.add(Tuple2.of("A", 3));
        list.add(Tuple2.of("A", 2));
        DataStreamSource<Tuple2<String, Integer>> streamSource = env.fromCollection(list);

        System.out.println(System.currentTimeMillis());
    }

    /**
     * 水位线
     */
    @Test
    public void watermark3() {
        env.setParallelism(10);
        DataStreamSource<Event> source = env.addSource(new ClickSource());
        source.print();
    }


}
