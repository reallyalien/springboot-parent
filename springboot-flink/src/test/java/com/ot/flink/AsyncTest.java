package com.ot.flink;

import com.alibaba.fastjson2.JSON;
import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import com.ot.flink.source.ClickSource2;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.e;

/**
 * 时间窗口测试
 */
public class AsyncTest extends BaseTest {


    /**
     * 动态数据 1
     */
    @Test
    public void async1() throws Exception {
        DataStreamSource<Event> source = env.fromCollection(eventList);
    }

}
