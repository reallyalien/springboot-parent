package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间窗口测试
 */
public class ReduceTest extends BaseTest {


    /**
     * 水位线
     */
    @Test
    public void watermark() {
        env.setParallelism(1);
        // 这里的 ClickSource()使用了之前自定义数据源小节中的 ClickSource()
        env.addSource(new ClickSource())
                // 将 Event 数据类型转换成元组类型
                .map(new MapFunction<Event, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(Event e) throws Exception {
                        return Tuple2.of(e.user, 1L);
                    }
                })
                .keyBy(r -> r.f0) // 使用用户名来进行分流
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> value1,
                                                       Tuple2<String, Long> value2) throws Exception {
                        // 每到一条数据，用户 pv 的统计值加 1
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                })
                .keyBy(r -> true) // 为每一条数据分配同一个 key，将聚合结果发送到一条流中
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> value1,
                                                       Tuple2<String, Long> value2) throws Exception {
                        // 将累加器更新为当前最大的 pv 统计值，然后向下游发送累加器的值
                        return value1.f1 > value2.f1 ? value1 : value2;
                    }
                })
                .print();
    }

    /**
     * 水位线
     */
    @Test
    public void watermark1() {
        List<Tuple2<String, Integer>> list = new ArrayList<>();
        list.add(Tuple2.of("A", 1));
        list.add(Tuple2.of("A", 3));
        list.add(Tuple2.of("A", 2));
        list.add(Tuple2.of("A", 27));
        list.add(Tuple2.of("B", 10));
        list.add(Tuple2.of("B", 5));
        list.add(Tuple2.of("B", 12));
        DataStreamSource<Tuple2<String, Integer>> streamSource = env.fromCollection(list);
        KeyedStream<Tuple2<String, Integer>, String> keyBy = streamSource.keyBy(e -> e.f0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> reduce1 = keyBy.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
            }
        });
//        reduce1.print();

        KeyedStream<Tuple2<String, Integer>, Boolean> keyedBy = reduce1.keyBy(e -> true);


        SingleOutputStreamOperator<Tuple2<String, Integer>> reduce = keyedBy.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
//                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                return value1.f1 > value2.f1 ? value1 : value2;

            }
        });
        reduce.print();
    }

}
