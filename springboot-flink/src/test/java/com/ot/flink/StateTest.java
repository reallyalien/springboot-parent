package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.entity.UrlViewCount;
import com.ot.flink.source.ClickSource;
import com.ot.flink.waterremark.CustomPeriodicGenerator;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.CloseableRegistry;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.runtime.execution.Environment;
import org.apache.flink.runtime.query.TaskKvStateRegistry;
import org.apache.flink.runtime.state.*;
import org.apache.flink.runtime.state.ttl.TtlTimeProvider;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;

/**
 * 状态测试
 */
public class StateTest extends BaseTest {


    @Test
    public void valueState() {
        env.setParallelism(1);
//        DataStreamSource<Event> streamSource = env.fromElements(
//                new Event("Bob", "./cart", 2000L),
//                new Event("Alice", "./prod?id=100", 3000L),
//                new Event("Alice", "./prod?id=200", 3500L),
//                new Event("Bob", "./prod?id=2", 2500L),
//                new Event("Alice", "./prod?id=300", 36000L),
//                new Event("Bob", "./home", 30000L),
//                new Event("Bob", "./prod?id=1", 23000L),
//                new Event("Bob", "./prod?id=3", 33000L)
//        );
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource());
        streamSource
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forMonotonousTimestamps().withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        return element.timestamp;
                    }
                }))
                .keyBy(e -> e.user)
                .process(new KeyedProcessFunction<String, Event, String>() {
                    ValueState<Long> countState;
                    ValueState<Long> timerTsState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        countState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("count", Long.class));
                        timerTsState = getRuntimeContext().getState(new ValueStateDescriptor<Long>("timerTs", Long.class));
                    }

                    @Override
                    public void processElement(Event value, Context ctx, Collector<String> out) throws Exception {
                        Long count = countState.value();
                        if (count == null) {
                            countState.update(1L);
                        } else {
                            countState.update(count + 1);
                        }

                        //注册定时器
                        if (timerTsState.value() == null) {
                            System.out.println("有" + value);
                            long l = value.timestamp + 10 * 1000L;
                            ctx.timerService().registerEventTimeTimer(l);
                            timerTsState.update(l);
                        } else {
                            System.out.println("空" + value);
                        }
                    }

                    @Override
                    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                        out.collect(ctx.getCurrentKey() + " pv: " + countState.value());
                        // 清空状态
//                        timerTsState.clear();
                    }
                }).print();
    }
}
