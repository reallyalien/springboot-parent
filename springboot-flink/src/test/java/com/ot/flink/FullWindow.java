package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.entity.UrlViewCount;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.RichAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.RichWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.table.planner.expressions.In;
import org.apache.flink.util.Collector;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Test;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: FullWindow
 * @Author wangtao
 * @Date 2023/9/13 9:36
 * @description:
 */
public class FullWindow extends BaseTest implements Serializable {


    @Test
    public void test1() {
        DataStreamSource<Event> source = env.addSource(new ClickSource());
        source.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                })).keyBy(e -> e.url)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .trigger(new Trigger<Event, TimeWindow>() {
                    @Override
                    public TriggerResult onElement(Event element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
                        ValueState<Boolean> isFirstEvent = ctx.getPartitionedState(new ValueStateDescriptor<Boolean>("first-event", Types.BOOLEAN));
                        if (isFirstEvent.value() == null) {
                            for (long i = window.getStart(); i < window.getEnd(); i += 1000L) {
                                ctx.registerEventTimeTimer(i);
                            }
                            isFirstEvent.update(true);
                        }
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.FIRE;
                    }

                    @Override
                    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
                        ValueState<Boolean> isFirstEvent = ctx.getPartitionedState(new ValueStateDescriptor<Boolean>("first-event", Types.BOOLEAN));
                        isFirstEvent.clear();
                    }
                })
                .process(new ProcessWindowFunction<Event, UrlViewCount, String, TimeWindow>() {
                    @Override
                    public void process(String s, ProcessWindowFunction<Event, UrlViewCount, String, TimeWindow>.Context context, Iterable<Event> elements, Collector<UrlViewCount> out) throws Exception {
                        out.collect(new UrlViewCount(s, elements.spliterator().getExactSizeIfKnown(), context.window().getStart(), context.window().getEnd()));
                    }
                })
                .print();

    }

    @Test
    public void test3() {
        DataStreamSource<Event> source = env.addSource(new ClickSource());
        source.assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO).withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .keyBy(e -> true)

//                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .window(GlobalWindows.create())
                .trigger(new Trigger<Event, GlobalWindow>() {
                    @Override
                    public TriggerResult onElement(Event element, long timestamp, GlobalWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onProcessingTime(long time, GlobalWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public TriggerResult onEventTime(long time, GlobalWindow window, TriggerContext ctx) throws Exception {
                        return TriggerResult.CONTINUE;
                    }

                    @Override
                    public void clear(GlobalWindow window, TriggerContext ctx) throws Exception {
                    }
                })
                .apply(new RichWindowFunction<Event, Integer, Boolean, GlobalWindow>() {
                    @Override
                    public void apply(Boolean s, GlobalWindow window, Iterable<Event> input, Collector<Integer> out) throws Exception {
                        int sum = 0;
                        for (Event value : input) {
                            sum += 1;
                        }
                        out.collect(sum);
                    }
                }).print();

    }


    /**
     * CONTINUE :什么也不做
     * FIRE：触发计算
     * PURGE：清除窗口中的元素
     * FIRE_AND_PURGE：触发计算并清空窗口中的元素
     */

    @Test
    public void test2() {
        DataStreamSource<Integer> source = env.fromElements(1, 2, 3);
        source
                .windowAll(GlobalWindows.create())
                .apply(new RichAllWindowFunction<Integer, Integer, GlobalWindow>() {
                    @Override
                    public void apply(GlobalWindow window, Iterable<Integer> values, Collector<Integer> out) throws Exception {
                        int sum = 0;
                        for (Integer value : values) {
                            sum += value;
                        }
                        out.collect(sum);
                    }
                })
                .print();
    }
}
