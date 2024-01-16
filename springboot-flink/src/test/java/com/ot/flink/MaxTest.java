package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.entity.UrlViewCount;
import com.ot.flink.source.ClickSource;
import com.ot.flink.waterremark.CustomPeriodicGenerator;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 时间窗口测试
 */
public class MaxTest extends BaseTest {


    /**
     * 水位线
     */
    @Test
    public void watermark() {
        List<Tuple2<String, Integer>> list = new ArrayList<>();
        list.add(Tuple2.of("A", 1));
        list.add(Tuple2.of("A", 3));
        list.add(Tuple2.of("A", 2));
//        list.add(Tuple3.of("B","xx1",3));
//        list.add(Tuple3.of("B","xx2",4));
//        list.add(Tuple3.of("B","xx3",5));
        DataStreamSource<Tuple2<String ,Integer>> streamSource = env.fromCollection(list);
        KeyedStream<Tuple2<String, Integer>, String> keyBy = streamSource.keyBy(e -> e.f0);
        keyBy.maxBy("f1").print();

    }


    /**
     * 水位线
     */
    @Test
    public void watermark1() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("key", "A");
        map1.put("age", 10);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("key", "B");
        map2.put("age", 20);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("key", "B");
        map3.put("age", 20);
        list.add(map1);
        list.add(map2);

        DataStreamSource<Map<String, Object>> streamSource = env.fromCollection(list);
        KeyedStream<Map<String, Object>, Object> key = streamSource.keyBy(e -> isNull(e.get("key")));
        WindowedStream<Map<String, Object>, Object, TimeWindow> window = key.window(SlidingProcessingTimeWindows.of(null, null));
        key.print();
    }

    /**
     * 水位线
     */
    @Test
    public void sum() {
        List<Tuple3<String, String, Integer>> list = new ArrayList<>();
        list.add(Tuple3.of("A", "xx1", 1));
        list.add(Tuple3.of("A", "xx2", 2));
        list.add(Tuple3.of("A", "xx3", 3));
//        list.add(Tuple3.of("B","xx1",3));
//        list.add(Tuple3.of("B","xx2",4));
//        list.add(Tuple3.of("B","xx3",5));
        DataStreamSource<Tuple3<String, String, Integer>> streamSource = env.fromCollection(list);
//        streamSource.keyBy(e -> e.f0).max(2).print();
        streamSource.keyBy(e -> e.f0).sum(2).print();

    }

    public Object isNull(Object o) {
        String s = Objects.toString(o);
        if (o == null) {
            return "null";
        }
        return o;
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "2", "3");
        HashSet<String> strings = new HashSet<>(list);
        System.out.println(strings.size());
    }
}
