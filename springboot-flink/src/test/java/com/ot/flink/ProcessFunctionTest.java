package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.function.BiFunction;


/**
 * 处理函数测试
 */
public class ProcessFunctionTest extends BaseTest {


    /**
     * 水位线
     */
    @Test
    public void watermark() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .process(new ProcessFunction<Event, String>() {
                    @Override
                    public void processElement(Event event, Context ctx, Collector<String> out) throws Exception {
                        if (event.user.equals("Mary")) {
                            out.collect(event.user);
                        } else if (event.user.equals("Bob")) {
                            out.collect(event.user);
                            out.collect(event.user);
                        }
                        System.out.println(ctx.timerService().currentWatermark());
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        super.onTimer(timestamp, ctx, out);
                    }
                });
    }

    /**
     * 水位线
     */
    @Test
    public void keyProcessFunction_processTime() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .keyBy(e -> true)
                .process(new KeyedProcessFunction<Boolean, Event, String>() {
                    @Override
                    public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                        long currTs = ctx.timerService().currentProcessingTime();
                        out.collect("数据到达，到达时间：" + new Timestamp(currTs));
                        //注册一个10s后的定时器
                        ctx.timerService().registerProcessingTimeTimer(currTs + 10 * 1000L);
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        out.collect("定时器触发，触发时间：" + new Timestamp(timestamp));
                    }
                })
                .print();
    }

    /**
     * 水位线
     */
    @Test
    public void keyProcessFunction_eventTime() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new SourceFunction<Event>() {
            @Override
            public void run(SourceContext<Event> ctx) throws Exception {
                // 直接发出测试数据
                ctx.collect(new Event("Mary", "./home", 1000L));
                // 为了更加明显，中间停顿 5 秒钟
                Thread.sleep(5000L);
                // 发出 10 秒后的数据
                ctx.collect(new Event("Mary", "./home", 11000L));
                Thread.sleep(5000L);
                // 发出 10 秒+1ms 后的数据
                ctx.collect(new Event("Alice", "./cart", 11001L));
                Thread.sleep(5000L);
            }

            @Override
            public void cancel() {

            }
        });
        streamSource
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                                    @Override
                                    public long extractTimestamp(Event element, long recordTimestamp) {
                                        return element.timestamp;
                                    }
                                }))
                .keyBy(e -> true)
                .process(new KeyedProcessFunction<Boolean, Event, String>() {
                    @Override
                    public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                        out.collect("数据到达，时间戳为：" + ctx.timestamp());
                        out.collect("数据到达，水位线为：" + ctx.timerService().currentWatermark() + "\n -------分割线-------");
                        // 注册一个 10 秒后的定时器
                        ctx.timerService().registerEventTimeTimer(ctx.timestamp() + 10 * 1000L);
                    }

                    //定时器触发的条件就是水位线推进到设定的时间
                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        out.collect("定时器触发，触发时间：" + timestamp);
                    }
                })
                .print();
    }

    /**
     * ProcessWindowFunction 解析 既是处理函数又是全窗口函数
     * <p>
     * 网站中一个非常经典的例子，就是实时统计一段时间内的热门 url。例如，需要统计最近
     * 10 秒钟内最热门的两个 url 链接，并且每 5 秒钟更新一次。我们知道，这可以用一个滑动窗口
     * 来实现，而“热门度”一般可以直接用访问量来表示。于是就需要开滑动窗口收集 url 的访问
     * 数据，按照不同的 url 进行统计，而后汇总排序并最终输出前两名。这其实就是著名的“Top N”
     * 问题。
     * <p>
     * 一种最简单的想法是，我们干脆不区分 url 链接，而是将所有访问数据都收集起来，统一
     * 进行统计计算。所以可以不做 keyBy，直接基于 DataStream 开窗，然后使用全窗口函数
     * ProcessAllWindowFunction 来进行处理。
     * 在窗口中可以用一个 HashMap 来保存每个 url 的访问次数，只要遍历窗口中的所有数据，
     * 自然就能得到所有 url 的热门度。最后把 HashMap 转成一个列表 ArrayList，然后进行排序、
     * 取出前两名输出就可以了。
     */
    @Test
    public void processWindowFunction_eventTime() {
        env.setParallelism(1);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .map(new MapFunction<Event, String>() {
                    @Override
                    public String map(Event value) throws Exception {
                        return value.url;
                    }
                })
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .process(new ProcessAllWindowFunction<String, String, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<String> elements, Collector<String> out) throws Exception {
                        Map<String, Long> urlCountMap = new HashMap<>();
                        //将数据保存到一个hashmap当中
                        elements.forEach(e -> urlCountMap.merge(e, 1L, Long::sum));
                        List<Tuple2<String, Long>> list = new ArrayList<>();
                        for (Map.Entry<String, Long> entry : urlCountMap.entrySet()) {
                            list.add(Tuple2.of(entry.getKey(), entry.getValue()));
                        }
                        //排序
                        list.sort((o1, o2) -> o2.f1.intValue() - o1.f1.intValue());
                        // 取排序后的前两名，构建输出结果
                        StringBuilder result = new StringBuilder();
                        result.append("========================================\n");
                        for (int i = 0; i < 2; i++) {
                            Tuple2<String, Long> temp = list.get(i);
                            String info = "浏览量 No." + (i + 1) +
                                    " url：" + temp.f0 +
                                    " 浏览量：" + temp.f1 +
                                    " 窗 口 开 始 时 间 ： " + new Timestamp(context.window().getStart()) +
                                    " 窗 口 结 束 时 间 ： " + new Timestamp(context.window().getEnd()) + "\n";
                            result.append(info);
                        }
                        result.append("========================================\n");
                        out.collect(result.toString());
                    }
                }).print();
    }

    /**
     * 测输出流
     */
    @Test
    public void SideOut() {
        env.setParallelism(1);
        OutputTag<String> outputTag = new OutputTag<String>("side-output") {
        };
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        SingleOutputStreamOperator<String> process = streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .map(new MapFunction<Event, String>() {
                    @Override
                    public String map(Event value) throws Exception {
                        return value.url;
                    }
                })
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .process(new ProcessAllWindowFunction<String, String, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<String> elements, Collector<String> out) throws Exception {
                        Map<String, Long> urlCountMap = new HashMap<>();
                        //将数据保存到一个hashmap当中
                        elements.forEach(e -> urlCountMap.merge(e, 1L, Long::sum));
                        List<Tuple2<String, Long>> list = new ArrayList<>();
                        for (Map.Entry<String, Long> entry : urlCountMap.entrySet()) {
                            list.add(Tuple2.of(entry.getKey(), entry.getValue()));
                        }
                        //排序
                        list.sort((o1, o2) -> o2.f1.intValue() - o1.f1.intValue());
                        // 取排序后的前两名，构建输出结果
                        StringBuilder result = new StringBuilder();
                        result.append("========================================\n");
                        for (int i = 0; i < 2; i++) {
                            Tuple2<String, Long> temp = list.get(i);
                            String info = "浏览量 No." + (i + 1) +
                                    " url：" + temp.f0 +
                                    " 浏览量：" + temp.f1 +
                                    " 窗 口 开 始 时 间 ： " + new Timestamp(context.window().getStart()) +
                                    " 窗 口 结 束 时 间 ： " + new Timestamp(context.window().getEnd()) + "\n";
                            result.append(info);
                        }
                        result.append("========================================\n");
                        out.collect(result.toString());

                        context.output(outputTag, result.toString());
                    }
                });
        //获取侧输出流
        DataStream<String> sideOutput = process.getSideOutput(outputTag);
        sideOutput.print();
    }
}
