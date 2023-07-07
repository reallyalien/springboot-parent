//package com.ot.flink;
//
//import com.ot.flink.entity.Event;
//import com.ot.flink.source.ClickSource;
//import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
//import org.apache.flink.api.common.eventtime.WatermarkStrategy;
//import org.apache.flink.api.common.functions.JoinFunction;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.api.java.tuple.Tuple2;
//import org.apache.flink.api.java.tuple.Tuple3;
//import org.apache.flink.streaming.api.datastream.ConnectedStreams;
//import org.apache.flink.streaming.api.datastream.DataStream;
//import org.apache.flink.streaming.api.datastream.DataStreamSource;
//import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
//import org.apache.flink.streaming.api.functions.ProcessFunction;
//import org.apache.flink.streaming.api.functions.co.CoMapFunction;
//import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
//import org.apache.flink.streaming.api.functions.source.SourceFunction;
//import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
//import org.apache.flink.streaming.api.windowing.time.Time;
//import org.apache.flink.util.Collector;
//import org.apache.flink.util.OutputTag;
//import org.junit.Test;
//
///**
// * 流转换
// */
//public class ConvertStreamTest extends BaseTest {
//
//
//    /**
//     * 相当于把流复制了3份进行筛选
//     */
//    @Test
//    public void test1() {
//        env.setParallelism(1);
//        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
//        SingleOutputStreamOperator<Event> mary = streamSource.filter(e -> e.user.equals("Mary"));
//        SingleOutputStreamOperator<Event> bob = streamSource.filter(e -> e.user.equals("Bob"));
//        SingleOutputStreamOperator<Event> other = streamSource.filter(e -> !e.user.equals("Mary") && !e.user.equals("Bob"));
//        mary.print("mary");
//        bob.print("bob");
//        other.print("other");
//    }
//
//    @Test
//    public void test2() {
//        // 定义输出标签，侧输出流的数据类型为三元组(user, url, timestamp)
//        OutputTag<Tuple3<String, String, Long>> maryTag = new OutputTag<Tuple3<String, String, Long>>("Mary-pv") {
//        };
//        OutputTag<Tuple3<String, String, Long>> bobTag = new OutputTag<Tuple3<String, String, Long>>("Bob-pv") {
//        };
//        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
//        SingleOutputStreamOperator<Event> process = streamSource.process(new ProcessFunction<Event, Event>() {
//            @Override
//            public void processElement(Event value, Context ctx, Collector<Event> out) {
//                if (value.user.equals("Mary")) {
//                    ctx.output(maryTag, new Tuple3<>(value.user, value.url, value.timestamp));
//                } else if (value.user.equals("Bob")) {
//                    ctx.output(bobTag, new Tuple3<>(value.user, value.url, value.timestamp));
//                } else {
//                    out.collect(value);
//                }
//            }
//        });
//        process.getSideOutput(maryTag).print("mary");
//        process.getSideOutput(bobTag).print("bob");
//        process.print("other");
//    }
//
//
//    /**
//     * 合流操作
//     */
//    @Test
//    public void unionTest() {
//        env.setParallelism(1);
//        DataStreamSource<Event> eventDataStreamSource1 = env.addSource(new SourceFunction<Event>() {
//            @Override
//            public void run(SourceContext<Event> ctx) throws Exception {
//                // 直接发出测试数据
//                ctx.collect(new Event("Mary", "./home", 1000L));
//                // 为了更加明显，中间停顿 5 秒钟
//                Thread.sleep(10000L);
//                // 发出 10 秒后的数据
//                ctx.collect(new Event("Mary", "./home", 3000L));
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//        });
//        DataStreamSource<Event> eventDataStreamSource2 = env.addSource(new SourceFunction<Event>() {
//            @Override
//            public void run(SourceContext<Event> ctx) throws Exception {
//                Thread.sleep(5000);
//                // 直接发出测试数据
//                ctx.collect(new Event("Mary", "./home", 2000L));
//                Thread.sleep(15000);
//                ctx.collect(new Event("Mary", "./home", 4000L));
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//        });
//        SingleOutputStreamOperator<Event> streamOperator1 = eventDataStreamSource1.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//            @Override
//            public long extractTimestamp(Event element, long recordTimestamp) {
//                return element.timestamp;
//            }
//        }));
////        streamOperator1.print("stream1");
//        SingleOutputStreamOperator<Event> streamOperator2 = eventDataStreamSource2.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
//            @Override
//            public long extractTimestamp(Event element, long recordTimestamp) {
//                return element.timestamp;
//            }
//        }));
////        streamOperator2.print("stream2");
//        streamOperator1.union(streamOperator2).process(new ProcessFunction<Event, String>() {
//            @Override
//            public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
//                out.collect("水位线" + ctx.timerService().currentWatermark());
//            }
//        }).print();
//    }
//
//    /**
//     * 连接
//     */
//    @Test
//    public void connect() {
//        DataStreamSource<Integer> streamSource1 = env.fromElements(1, 2, 3);
//        DataStreamSource<Long> streamSource2 = env.fromElements(1L, 2L, 3L);
//        ConnectedStreams<Integer, Long> streams = streamSource1.connect(streamSource2);
//        streams.map(new CoMapFunction<Integer, Long, String>() {
//            @Override
//            public String map1(Integer value) throws Exception {
//                return "integer " + value;
//            }
//
//            @Override
//            public String map2(Long value) throws Exception {
//                return "long " + value;
//            }
//        }).print();
//    }
//
//    /**
//     * 在电商网站中，往往需要统计用户不同行为之间的转化，这就需要对不同的行为数据流，
//     * 按照用户 ID 进行分组后再合并，以分析它们之间的关联。如果这些是以固定时间周期（比如
//     * 1 小时）来统计的，那我们就可以使用窗口 join 来实现这样的需求
//     * join
//     * <p>
//     * 窗口连结是笛卡儿积
//     */
//    @Test
//    public void window_join() {
//        SingleOutputStreamOperator<Tuple2<String, Long>> stream1 = env
//                .fromElements(
//                        Tuple2.of("a", 1000L),
//                        Tuple2.of("b", 1000L),
//                        Tuple2.of("a", 2000L),
//                        Tuple2.of("b", 2000L)
//                ).assignTimestampsAndWatermarks(
//                        WatermarkStrategy.<Tuple2<String, Long>>forMonotonousTimestamps().withTimestampAssigner(
//                                new SerializableTimestampAssigner<Tuple2<String, Long>>() {
//                                    @Override
//                                    public long extractTimestamp(Tuple2<String,
//                                            Long> stringLongTuple2, long l) {
//                                        return stringLongTuple2.f1;
//                                    }
//                                }
//                        )
//                );
//        DataStream<Tuple2<String, Long>> stream2 = env
//                .fromElements(
//                        Tuple2.of("a", 3000L),
//                        Tuple2.of("b", 3000L),
//                        Tuple2.of("a", 4000L),
//                        Tuple2.of("b", 4000L)
//                )
//                .assignTimestampsAndWatermarks(
//                        WatermarkStrategy
//                                .<Tuple2<String, Long>>forMonotonousTimestamps().withTimestampAssigner(
//                                new SerializableTimestampAssigner<Tuple2<String, Long>>() {
//                                    @Override
//                                    public long extractTimestamp(Tuple2<String,
//                                            Long> stringLongTuple2, long l) {
//                                        return stringLongTuple2.f1;
//                                    }
//                                }
//
//                        )
//                );
//        stream1
//                .join(stream2)
//                .where(r -> r.f0)
//                .equalTo(r -> r.f0)
//                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
//                .apply(new JoinFunction<Tuple2<String, Long>, Tuple2<String, Long>, String>() {
//                    @Override
//                    public String join(Tuple2<String, Long> first, Tuple2<String, Long> second) throws Exception {
//                        return first + "==>" + second;
//                    }
//                })
//                .print();
//
//    }
//
//    /**
//     * 在电商网站中，某些用户行为往往会有短时间内的强关联。我们这里举一个例子，我们有
//     * 两条流，一条是下订单的流，一条是浏览数据的流。我们可以针对同一个用户，来做这样一个
//     * 联结。也就是使用一个用户的下订单的事件和这个用户的最近十分钟的浏览数据进行一个联结
//     * 查询
//     * <p>
//     * 间隔连结
//     */
//    @Test
//    public void interval_join() {
//        SingleOutputStreamOperator<Tuple3<String, String, Long>> orderStream = env
//                .fromElements(
//                        Tuple3.of("Mary", "order-1", 5000L),
//                        Tuple3.of("Alice", "order-2", 5000L),
//                        Tuple3.of("Bob", "order-3", 20000L),
//                        Tuple3.of("Alice", "order-4", 20000L),
//                        Tuple3.of("Cary", "order-5", 51000L)
//                ).assignTimestampsAndWatermarks(
//                        WatermarkStrategy.<Tuple3<String, String, Long>>forMonotonousTimestamps().withTimestampAssigner(
//                                new SerializableTimestampAssigner<Tuple3<String, String, Long>>() {
//                                    @Override
//                                    public long extractTimestamp(Tuple3<String, String,
//                                            Long> tuple3, long l) {
//                                        return tuple3.f2;
//                                    }
//                                }
//                        )
//                );
//        SingleOutputStreamOperator<Event> clickStream = env
//                .fromElements(
//                        new Event("Bob", "./cart", 2000L),
//                        new Event("Alice", "./prod?id=100", 3000L),
//                        new Event("Alice", "./prod?id=200", 3500L),
//                        new Event("Bob", "./prod?id=2", 2500L),
//                        new Event("Alice", "./prod?id=300", 36000L),
//                        new Event("Bob", "./home", 30000L),
//                        new Event("Bob", "./prod?id=1", 23000L),
//                        new Event("Bob", "./prod?id=3", 33000L)
//                ).assignTimestampsAndWatermarks(
//                        WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner(
//                                new SerializableTimestampAssigner<Event>() {
//                                    @Override
//                                    public long extractTimestamp(Event event, long l) {
//                                        return event.timestamp;
//                                    }
//                                }
//                        )
//                );
//        orderStream.keyBy(e -> e.f0)
//                .intervalJoin(clickStream.keyBy(d -> d.user))
//                .between(Time.seconds(-5), Time.seconds(10))
//                .process(new ProcessJoinFunction<Tuple3<String, String, Long>, Event, String>() {
//                    @Override
//                    public void processElement(Tuple3<String, String, Long> left, Event right, Context ctx, Collector<String> out) throws Exception {
//                        out.collect(right + " => " + left);
//                    }
//                }).print();
//
//    }
//}
