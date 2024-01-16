package com.ot.flink;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.base.IntSerializer;
import org.apache.flink.api.common.typeutils.base.LongSerializer;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.RichAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: CountAndSizeTrigger
 * @Author wangtao
 * @Date 2023/9/13 10:37
 * @description:
 */
@Data
@Slf4j
public class CountAndSizeTrigger<T> extends Trigger<T, TimeWindow> {

    private static final String DATA_COUNT_STATE_NAME = "dataCountState";

    private static final String DATA_SIZE_STATE_NAME = "dataSizeState";

    // 窗口最大数据条数
    private int maxCount;

    // 窗口最大数据字节数
    private int maxSize;

    // 时间语义：event time、process time
    private TimeCharacteristic timeType;

    // 用于储存窗口当前数据条数的状态对象
    private ReducingStateDescriptor<Long> countStateDescriptor = new ReducingStateDescriptor(DATA_COUNT_STATE_NAME, new ReduceFunction<Long>() {
        @Override
        public Long reduce(Long value1, Long value2) throws Exception {
            return value1 + value2;
        }
    }, LongSerializer.INSTANCE);

    //用于储存窗口当前数据字节数的状态对象
    private ReducingStateDescriptor<Integer> sizeStateDescriptor = new ReducingStateDescriptor(DATA_SIZE_STATE_NAME, new ReduceFunction<Long>() {
        @Override
        public Long reduce(Long value1, Long value2) throws Exception {
            return value1 + value2;
        }
    }, IntSerializer.INSTANCE);


    private CountAndSizeTrigger(int maxCount, int maxSize, TimeCharacteristic timeType) {
        this.maxCount = maxCount;
        this.maxSize = maxSize;
        this.timeType = timeType;
    }

    /**
     * 触发计算，计算结束后清空窗口内的元素
     *
     * @param window 窗口
     * @param ctx    上下文
     */
    private TriggerResult fireAndPurge(TimeWindow window, TriggerContext ctx) throws Exception {
        clear(window, ctx);
        return TriggerResult.FIRE_AND_PURGE;
    }

    @Override
    public TriggerResult onElement(T element, long timestamp, TimeWindow window, TriggerContext ctx) throws Exception {
        ReducingState<Long> countState = ctx.getPartitionedState(countStateDescriptor);
        ReducingState<Integer> sizeState = ctx.getPartitionedState(sizeStateDescriptor);

        Map<String, JSONArray> map = stringToMap(element.toString());
        if (map != null) {
            for (Map.Entry<String, JSONArray> entry : map.entrySet()) {
                JSONArray value = entry.getValue();
                countState.add(Long.valueOf(value.size()));
            }
        } else {
            countState.add(0L);
        }
        int length = String.valueOf(element).getBytes("utf-8").length;
        sizeState.add(length);
        // 注册定时器
        ctx.registerProcessingTimeTimer(window.maxTimestamp());

        if (countState.get() >= maxCount) {
            log.info("fire count {} ", countState.get());
            return fireAndPurge(window, ctx);
        }
        if (sizeState.get() >= maxSize) {
            log.info("fire size {} ", sizeState.get());
            return fireAndPurge(window, ctx);
        } else {
            return TriggerResult.CONTINUE;
        }
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        if (timeType != TimeCharacteristic.ProcessingTime) {
            return TriggerResult.CONTINUE;
        }
        log.info("fire time {} ", time);
        return fireAndPurge(window, ctx);
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) throws Exception {
        if (timeType != TimeCharacteristic.EventTime) {
            return TriggerResult.CONTINUE;
        }
        if (time >= window.getEnd()) {
            return TriggerResult.CONTINUE;
        } else {
            log.info("fire with event tiem: " + time);
            return fireAndPurge(window, ctx);
        }
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) throws Exception {
        ctx.getPartitionedState(countStateDescriptor).clear();
        ctx.getPartitionedState(sizeStateDescriptor).clear();
    }


    // 数据处理，可根据需要修改
    private Map<String, JSONArray> stringToMap(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        String string = str.substring(1, str.length() - 1).replaceAll(" ", "");
        Map<String, JSONArray> map = new HashMap<>();
        String[] split = string.split("=");
        if (split.length < 2) {
            return null;
        } else {
            String key = split[0];
            String value = string.substring(string.indexOf("=") + 1);
            map.put(key, JSON.parseArray(value));
        }
        return map;
    }

    /**
     * 初始化触发器，默认使用processTime
     *
     * @param maxCount 最大数据条数
     * @param maxSize  最大数据字节数
     */
    public static CountAndSizeTrigger creat(int maxCount, int maxSize) {
        return new CountAndSizeTrigger(maxCount, maxSize, TimeCharacteristic.ProcessingTime);
    }

    /**
     * 初始化触发器
     *
     * @param maxCount 最大数据条数
     * @param maxSize  最大数据字节数
     * @param timeType 事件类型
     */
    public static CountAndSizeTrigger creat(int maxCount, int maxSize, TimeCharacteristic timeType) {
        return new CountAndSizeTrigger(maxCount, maxSize, timeType);
    }


    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> source = env.addSource(new ClickSource());
        SingleOutputStreamOperator demo = source
                .windowAll(GlobalWindows.create())
                .trigger(new CountAndSizeTrigger(1000, 1024, TimeCharacteristic.ProcessingTime))
                        .apply(new RichAllWindowFunction() {
                            @Override
                            public void apply(Window window, Iterable values, Collector out) throws Exception {
                                out.collect(1);
                            }
                        });
        demo.print();
        env.execute();
    }
}
