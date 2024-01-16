
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
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 时间窗口测试
 */
public class GroupMultiKeyTest extends BaseTest {


    /**
     * eg:
     * 下面来看一个具体例子。我们知道，在电商网站中，PV（页面浏览量）和 UV（独立访客
     * 数）是非常重要的两个流量指标。一般来说，PV 统计的是所有的点击量；而对用户 id 进行去
     * 重之后，得到的就是 UV。所以有时我们会用 PV/UV 这个比值，来表示“人均重复访问量”，
     * 也就是平均每个用户会访问多少次页面，这在一定程度上代表了用户的粘度。
     * 聚合函数
     */
    @Test
    public void aggFunctionCount() {
        List<Tuple3<String, String, Integer>> list = new ArrayList<>();
        list.add(Tuple3.of("A", "a", 1));
        list.add(Tuple3.of("A", "b", 3));
        list.add(Tuple3.of("A", "c", 2));
        DataStreamSource<Tuple3<String, String, Integer>> streamSource = env.fromCollection(list);
        KeyedStream<Tuple3<String, String, Integer>, String> keyBy = streamSource.keyBy(e -> e.f0 + e.f1);
        keyBy.window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .aggregate(new AggregateFunction<Tuple3<String, String, Integer>, Tuple3<String, String, Integer>, Object>() {
                    @Override
                    public Tuple3<String, String, Integer> createAccumulator() {
                        return null;
                    }

                    @Override
                    public Tuple3<String, String, Integer> add(Tuple3<String, String, Integer> value, Tuple3<String, String, Integer> accumulator) {
                        return null;
                    }

                    @Override
                    public Object getResult(Tuple3<String, String, Integer> accumulator) {
                        return null;
                    }

                    @Override
                    public Tuple3<String, String, Integer> merge(Tuple3<String, String, Integer> a, Tuple3<String, String, Integer> b) {
                        return null;
                    }
                });
    }

}
