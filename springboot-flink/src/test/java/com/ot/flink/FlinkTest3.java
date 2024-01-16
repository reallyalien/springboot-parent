package com.ot.flink;


import com.ot.flink.entity.Event;
import com.ot.flink.function.MyFlatMap;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static org.apache.flink.table.api.Expressions.$;

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
        SingleOutputStreamOperator<String> map = dataStreamSource.map(String::toUpperCase);
        map.print();
        map.map(String::toLowerCase).print();
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

    @Test
    public void richMap3() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(3);
        DataStreamSource<String> dataStreamSource = env.fromElements("1", "a", "c", "d");
        dataStreamSource.flatMap(new MyFlatMap()).print();
        env.execute();
    }

    @Test
    public void richMap4() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        InputStream is = FlinkTest3.class.getClassLoader().getResourceAsStream("input/1.txt");
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String s = sb.toString();
        String[] split = s.split("\\+I\\[");
        List<Tuple5<String, String, String, String, String>> list = new ArrayList<>();
        for (String s1 : split) {
            if (s1.isEmpty()) {
                continue;
            }
            int i = s1.lastIndexOf("]");
            try {
                s1 = s1.substring(0, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] split1 = s1.split(",");
            list.add(Tuple5.of(split1[0].trim(), split1[1].trim(), split1[2].trim(), split1[3].trim(), split1[4].trim()));
        }
        Collections.sort(list, new Comparator<Tuple5<String, String, String, String, String>>() {
            @Override
            public int compare(Tuple5<String, String, String, String, String> o1, Tuple5<String, String, String, String, String> o2) {
                return Integer.parseInt(o1.f0) - Integer.parseInt(o2.f0);
            }
        });
        DataStreamSource<Tuple5<String, String, String, String, String>> streamSource = env.fromCollection(list);
        KeyedStream<Tuple5<String, String, String, String, String>, String> stream = streamSource.keyBy(e -> e.f0);
        SingleOutputStreamOperator<Tuple5<String, String, String, String, String>> reduce = stream.reduce(new ReduceFunction<Tuple5<String, String, String, String, String>>() {
            @Override
            public Tuple5<String, String, String, String, String> reduce(Tuple5<String, String, String, String, String> value1, Tuple5<String, String, String, String, String> value2) throws Exception {
                int arity = value1.getArity();
                for (int i = 0; i < arity; i++) {
                    Object field1 = value1.getField(i);
                    Object field2 = value2.getField(i);
                    if (field1.equals("null") && field2 != null) {
                        value1.setField(field2, i);
                    }
                }
                return value1;
            }
        });
        reduce.print();
//        SingleOutputStreamOperator<Tuple5<String, String, String, String, String>> aNull = stream.process(new KeyedProcessFunction<String, Tuple5<String, String, String, String, String>, Tuple5<String, String, String, String, String>>() {
//            @Override
//            public void processElement(Tuple5<String, String, String, String, String> value, Context ctx, Collector<Tuple5<String, String, String, String, String>> out) throws Exception {
//                int arity = value.getArity();
//                boolean flag = true;
//                for (int i = 0; i < arity; i++) {
//                    Object field = value.getField(i);
//                    if (field.equals("null")) {
//                        flag = false;
//                    }
//                }
//                if (flag) {
//                    out.collect(value);
//                }
//            }
//        });
//        aNull.print();
        env.execute();
    }

    @Test
    public void test1() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<Tuple2<String, Integer>> source = env.fromElements(
                Tuple2.of("a", 1),
                Tuple2.of("a", 2),
                Tuple2.of("a", 3),

                Tuple2.of("b", 1),
                Tuple2.of("b", 100),
                Tuple2.of("c", 1000)
        );
//        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
//        tableEnv.createTemporaryView("eventTable", source, $("user"), $("aaa"));
//        Table table = tableEnv.sqlQuery("select user,max(aaa) from eventTable group by user");
//        DataStream<Row> rowDataStream = tableEnv.toChangelogStream(table);
//        rowDataStream.print();
        KeyedStream<Tuple2<String, Integer>, String> keyBy = source.keyBy(e -> e.f0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> f1 = keyBy.maxBy(1);
        f1.print();

//                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
//                    @Override
//                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
//                        Tuple2<String, Integer> of = Tuple2.of(value1.f0, value1.f1 + value2.f1);
//                        return of;
//                    }
//                });
//        reduce.print();

        env.execute();
    }

    @Test
    public void test2() throws Exception {
        List<Tuple2<String, Integer>> list = Arrays.asList(
                Tuple2.of("a", 1),
                Tuple2.of("a", 2),
                Tuple2.of("a", 3)
        );
        Optional<Tuple2<String, Integer>> reduce = list.stream().reduce(new BinaryOperator<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> apply(Tuple2<String, Integer> stringIntegerTuple2, Tuple2<String, Integer> stringIntegerTuple22) {
                return Tuple2.of(stringIntegerTuple2.f0, stringIntegerTuple2.f1 + stringIntegerTuple2.f1);
            }
        });
        Tuple2<String, Integer> stringIntegerTuple2 = reduce.get();
        System.out.println(stringIntegerTuple2);
    }
}
