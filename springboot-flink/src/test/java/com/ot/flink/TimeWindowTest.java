package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.entity.UrlViewCount;
import com.ot.flink.source.ClickSource;
import com.ot.flink.waterremark.CustomPeriodicGenerator;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;

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
public class TimeWindowTest extends BaseTest {


    /**
     * 水位线
     */
    @Test
    public void watermark() {
        DataStreamSource<Integer> streamSource = env.fromElements(1, 2, 3);
        streamSource.assignTimestampsAndWatermarks(new WatermarkStrategy<Integer>() {
            @Override
            public WatermarkGenerator<Integer> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return null;
            }
        });
    }

    /**
     * 处理有序数据 ,时间戳单增长，所以永远不会出现迟到数据的问题，这是周期性生成水位线最简单的场景,
     * 直接调用
     * WatermarkStrategy.forMonotonousTimestamps()方法就可以实现。简单来说，就是直接拿当前最
     * 大的时间戳作为水位线就可以了
     * <p>
     * 注意：这里需要注意的是，时间戳和水位线的单位，必须都是毫秒。
     */
    @Test
    public void watermarkSort() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
            //将数据当中的时间戳字段提取出来，分配给数据元素
            @Override
            public long extractTimestamp(Event element, long recordTimestamp) {
                return element.timestamp;
            }
        }));
    }

    /**
     * 处理无序数据
     */
    @Test
    public void watermarkNoSort() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource.assignTimestampsAndWatermarks
                (WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(5000)).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }));
    }

    /**
     * 自定义水位生成器
     */
    @Test
    public void watermarkCustomer() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource.assignTimestampsAndWatermarks(new WatermarkStrategy<Event>() {
            @Override
            public WatermarkGenerator<Event> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
                return new CustomPeriodicGenerator();
            }

            @Override
            public TimestampAssigner<Event> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
                return new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                };
            }
        });
    }

    /**
     * 滚动处理时间窗口
     */
    @Test
    public void rollTimeWindow() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource
                .keyBy(Event::getUser)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5))); //长度为5s的滚动事件窗口

    }

    /**
     * 滑动时间窗口
     */
    @Test
    public void slideTimeWindow() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        WindowedStream<Event, String, TimeWindow> window = streamSource
                .keyBy(Event::getUser)
                .window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5)));//长度为5s的滚动事件窗口

    }

    /**
     * 会话时间窗口
     */
    @Test
    public void sessionTimeWindow() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource
                .keyBy(Event::getUser)
                .window(ProcessingTimeSessionWindows.withGap(Time.seconds(5)));

    }

    /**
     * 会话时间窗口
     */
    @Test
    public void sessionTimeWindow2() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource
                .keyBy(Event::getUser)
                .window(ProcessingTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor<Event>() {
                    @Override
                    public long extract(Event element) {
                        return element.timestamp; //动态设置session时长
                    }
                }));

    }

    /**
     * 计数窗口
     */
    @Test
    public void countWindow() {
        DataStreamSource<Event> streamSource = env.fromCollection(eventList);
        streamSource
                .keyBy(Event::getUser)
                .countWindow(10, 3)
                .process(new ProcessWindowFunction<Event, Event, String, GlobalWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<Event> elements, Collector<Event> out) throws Exception {
                        elements.forEach(event -> out.collect(event));
                    }
                })
                .print();

    }

    /**
     * reduceFunction 增量聚合
     */
    @Test
    public void reduceFunction() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .map(new MapFunction<Event, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(Event value) throws Exception {
                        return Tuple2.of(value.user, 1L);
                    }
                })
                .keyBy(e -> e.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .reduce(new ReduceFunction<Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> reduce(Tuple2<String, Long> value1, Tuple2<String, Long> value2) throws Exception {
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                })
                .print();
    }

    /**
     * eg:
     * 下面来看一个具体例子。我们知道，在电商网站中，PV（页面浏览量）和 UV（独立访客
     * 数）是非常重要的两个流量指标。一般来说，PV 统计的是所有的点击量；而对用户 id 进行去
     * 重之后，得到的就是 UV。所以有时我们会用 PV/UV 这个比值，来表示“人均重复访问量”，
     * 也就是平均每个用户会访问多少次页面，这在一定程度上代表了用户的粘度。
     * 聚合函数
     */
    @Test
    public void aggFunction() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .keyBy(e -> true)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(2)))//事件事件处理窗口
                .aggregate(new AggregateFunction<Event, Tuple2<HashSet<String>, Long>, Double>() {
                    //创建累加器,hashset保存用户id，而且自动去重
                    @Override
                    public Tuple2<HashSet<String>, Long> createAccumulator() {
                        return Tuple2.of(new HashSet<>(), 0L);
                    }

                    //属于本窗口的数据，来一次累计一次,并返回累加器
                    @Override
                    public Tuple2<HashSet<String>, Long> add(Event value, Tuple2<HashSet<String>, Long> accumulator) {
                        accumulator.f0.add(value.user);
                        return Tuple2.of(accumulator.f0, accumulator.f1 + 1L);
                    }

                    //窗口关闭时，增量聚合结束，将计算结果发送到下游
                    @Override
                    public Double getResult(Tuple2<HashSet<String>, Long> accumulator) {
                        //f1其实就是pv,f0是所有访问的用户集合
                        return (double) accumulator.f1 / accumulator.f0.size();
                    }

                    @Override
                    public Tuple2<HashSet<String>, Long> merge(Tuple2<HashSet<String>, Long> a, Tuple2<HashSet<String>, Long> b) {
                        return null;
                    }
                }).print();
    }


    /**
     * 全窗口函数
     * window function
     * 下面是一个电商网站统计每小时 UV 的例子：
     */
    @Test
    public void processWindowFunction() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .keyBy(e -> true)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .process(new ProcessWindowFunction<Event, String, Boolean, TimeWindow>() {
                    @Override
                    public void process(Boolean aBoolean, Context context, Iterable<Event> elements, Collector<String> out) throws Exception {
                        System.out.println(aBoolean);
                        HashSet<String> userSet = new HashSet<>();
                        for (Event element : elements) {
                            userSet.add(element.user);
                        }
                        System.out.println(userSet);
                        long start = context.window().getStart();
                        long end = context.window().getEnd();
                        out.collect("窗口 : " + new Timestamp(start) + " ~ " + new
                                Timestamp(end)
                                + " 的独立访客数量是：" + userSet.size());
                    }
                })
                .print();

    }

    /**
     * 全窗口函数
     * window function
     * 下面是一个电商网站统计每小时 UV 的例子：
     */
    @Test
    public void processWindowFunction11() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .keyBy(value -> true);

    }

    /**
     * 下面我们举一个具体的实例来说明。在网站的各种统计指标中，一个很重要的统计指标就
     * 是热门的链接；想要得到热门的 url，前提是得到每个链接的“热门度”。一般情况下，可以用
     * url 的浏览量（点击量）表示热门度。我们这里统计 10 秒钟的 url 浏览量，每 5 秒钟更新一次；
     * 另外为了更加清晰地展示，还应该把窗口的起始结束时间一起输出。我们可以定义滑动窗口，
     * 并结合增量聚合函数和全窗口函数来得到统计结果
     */
    @Test
    public void aggProcessWindowFunction() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.timestamp;
                            }
                        }))
                .keyBy(e -> e.url)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .aggregate(
                        new AggregateFunction<Event, Long, Long>() {
                            @Override
                            public Long createAccumulator() {
                                return 0L;
                            }

                            @Override
                            public Long add(Event value, Long accumulator) {
                                return accumulator + 1;
                            }

                            @Override
                            public Long getResult(Long accumulator) {
                                return accumulator;
                            }

                            @Override
                            public Long merge(Long a, Long b) {
                                return null;
                            }
                        },
                        new ProcessWindowFunction<Long, UrlViewCount, String, TimeWindow>() {
                            @Override
                            public void process(String s, Context context, Iterable<Long> elements, Collector<UrlViewCount> out) throws Exception {
                                long start = context.window().getStart();
                                long end = context.window().getEnd();
                                out.collect(new UrlViewCount(s, elements.iterator().next(), start, end));
                            }
                        })
                .print();
    }


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
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .keyBy(e -> true)
                .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))//事件事件处理窗口
                .aggregate(new AggregateFunction<Event, List<Event>, Integer>() {
                    @Override
                    public List<Event> createAccumulator() {
                        return new ArrayList<>();
                    }

                    @Override
                    public List<Event> add(Event value, List<Event> accumulator) {
                        accumulator.add(value);
                        return accumulator;
                    }

                    @Override
                    public Integer getResult(List<Event> accumulator) {
                        System.out.println(accumulator.stream().map(e -> e.user).collect(Collectors.toList()));
                        return accumulator.size();
                    }

                    @Override
                    public List<Event> merge(List<Event> a, List<Event> b) {
                        return null;
                    }
                }, new ProcessWindowFunction<Integer, Integer, Boolean, TimeWindow>() {
                    @Override
                    public void process(Boolean aBoolean, Context context, Iterable<Integer> elements, Collector<Integer> out) throws Exception {
                        long start = context.window().getStart();
                        long end = context.window().getEnd();
                        System.out.println(new Date(start) + "xx" + new Date(end));
                    }
                })
                .print();
    }

}
