package com.ot.flink;

import com.ot.flink.entity.CartInfo;
import com.ot.flink.entity.Order;
import com.ot.flink.partition.SinglePartition;
//import com.ot.flink.sink.Sink;
//import com.ot.flink.source.HdfsSource;
import com.ot.flink.source.SingleSource;
import com.ot.flink.source.SingleSource2;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import org.apache.flink.util.Collector;
import org.junit.Test;
import org.omg.CORBA.INTERNAL;

import java.util.Arrays;

public class FlinkTest1 {


    @Test
    public void test1() throws Exception {
        /**
         * 大致的流程就分为
         * 1.环境准备
         * 设置运行模式
         * 2.加载数据源
         * 3.数据转换
         * 4.数据输出
         * 5.执行程序
         */

        //准备环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置运行模式
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        //加载数据源
        //1.从文件当中读取
        //env.readTextFile()
        //2.从socket当中读取
        //env.socketTextStream()
        //从集合当中获取
        DataStreamSource<String> streamSource = env.fromElements("java,scala,php,c++", "java,scala,php", "java,scala", "java");
        //数据转换
        SingleOutputStreamOperator<String> flatMap = streamSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                String[] split = s.split(",");
                for (String s1 : split) {
                    collector.collect(s1);
                }
            }
        });
        SingleOutputStreamOperator<String> source = flatMap.map(new MapFunction<String, String>() {
            @Override
            public String map(String s) throws Exception {
                return s.toUpperCase();
            }
        });
        //数据输出
        source.print();
        //执行程序
        env.execute();
    }


    /**
     * 自定义数据源
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        SingleSource singleSource = new SingleSource();
        SinglePartition singlePartition = new SinglePartition();
        //准备环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置运行模式
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        //为流式作业启用检查点 以毫秒为单位 流式数据流的分布式状态将被定期快照
        env.enableCheckpointing(5000);
        DataStreamSource<String> source = env.addSource(singleSource, "简单自定义数据源");

        SingleOutputStreamOperator<Object> process = source.process(new ProcessFunction<String, Object>() {
            @Override
            public void processElement(String value, ProcessFunction<String, Object>.Context ctx, Collector<Object> out) throws Exception {
                out.collect(value);
                out.collect(value);
            }
        });
        process.print();
        env.execute();
    }

//    /**
//     * 自定义输入hdfs数据源
//     * 自定义输出
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test3() throws Exception {
//        HdfsSource hdfsSource = new HdfsSource();
//        Sink sink = new Sink();
//        //准备环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        //设置运行模式
//        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
//        //为流式作业启用检查点 以毫秒为单位 流式数据流的分布式状态将被定期快照
//        env.enableCheckpointing(5000);
//        DataStreamSource<String> source = env.addSource(hdfsSource, "简单自定义数据源");
//        DataStream<String> stream = source.map(e -> e);
//        stream.addSink(sink);
//        env.execute();
//    }

//    /**
//     * 累加器
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test4() throws Exception {
//        SingleSource singleSource = new SingleSource();
//        Sink sink = new Sink();
//        //准备环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        //设置运行模式
//        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
//        //设置并行度
//        //新建累加器
//        IntCounter intCounter = new IntCounter();
//        env.setParallelism(1);
//        //为流式作业启用检查点 以毫秒为单位 流式数据流的分布式状态将被定期快照
//        env.enableCheckpointing(5000);
//        DataStreamSource<String> source = env.addSource(singleSource, "简单自定义数据源");
//        source.map(new RichMapFunction<String, String>() {
//
//            @Override
//            public String map(String value) throws Exception {
//                intCounter.add(1);
//                return value;
//            }
//
//            @Override
//            public void open(Configuration parameters) throws Exception {
//                super.open(parameters);
//                getRuntimeContext().addAccumulator("counter", intCounter);
//            }
//        }).addSink(sink);
//        JobExecutionResult result = env.execute();
//        Integer counter = result.getAccumulatorResult("counter");
//        System.out.println(counter);
//    }

    /**
     * 时间窗口
     *
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        SingleSource2 singleSource = new SingleSource2();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置运行模式
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        //为流式作业启用检查点 以毫秒为单位 流式数据流的分布式状态将被定期快照
        env.enableCheckpointing(5000);
        DataStreamSource<String> source = env.addSource(singleSource, "简单自定义数据源");
        SingleOutputStreamOperator<CartInfo> map = source.map(new MapFunction<String, CartInfo>() {
            @Override
            public CartInfo map(String s) throws Exception {
                String[] split = s.split(",");
                return new CartInfo(split[0], Integer.valueOf(split[1]));
            }
        });
//        //需求1：每5s统计一次，最近5s内，各个路口，信号灯通过的红绿灯汽车的数量-基于时间的滚动窗口
        SingleOutputStreamOperator<CartInfo> result1 = map.keyBy(CartInfo::getSensorId)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5))).sum("count");
        result1.print();

        //需求2:每5秒钟统计一次，最近10秒钟内，各个路口/信号灯通过红绿灯汽车的数量--基于时间的滑动窗口
//        SingleOutputStreamOperator<CartInfo> result2 = map.keyBy(CartInfo::getSensorId).window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5))).sum("count");
//        result2.print();
        env.execute();
    }


    /**
     * 数量窗口
     *
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
        SingleSource2 singleSource = new SingleSource2();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置运行模式
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        //为流式作业启用检查点 以毫秒为单位 流式数据流的分布式状态将被定期快照
        env.enableCheckpointing(5000);
        DataStreamSource<String> source = env.addSource(singleSource, "简单自定义数据源");
        SingleOutputStreamOperator<CartInfo> map = source.map(new MapFunction<String, CartInfo>() {
            @Override
            public CartInfo map(String s) throws Exception {
                String[] split = s.split(",");
                return new CartInfo(split[0], Integer.valueOf(split[1]));
            }
        });
// * 需求1:统计在最近5条消息中,各自路口通过的汽车数量,相同的key每出现5次进行统计--基于数量的滚动窗口
        SingleOutputStreamOperator<CartInfo> result1 = map.keyBy(CartInfo::getSensorId)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5))).sum("count");
        result1.print();

        //需求2:每5秒钟统计一次，最近10秒钟内，各个路口/信号灯通过红绿灯汽车的数量--基于时间的滑动窗口
//        SingleOutputStreamOperator<CartInfo> result2 = map.keyBy(CartInfo::getSensorId).window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5))).sum("count");
//        result2.print();
        env.execute();
    }

//    /**
//     * table、sql
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test7() throws Exception {
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
//        //source
//        DataStreamSource<Order> orderA = env.fromCollection(Arrays.asList(
//                new Order(1L, "beer", 3),
//                new Order(1L, "diaper", 4),
//                new Order(3L, "rubber", 2))
//        );
//
//        DataStreamSource<Order> orderB = env.fromCollection(Arrays.asList(
//                new Order(2L, "pen", 3),
//                new Order(3L, "rubber", 4),
//                new Order(4L, "bber", 1))
//        );
//
//        //注册表
//        // convert DataStream to Table
//        Table tableA = tEnv.fromDataStream(orderA);
//        System.out.println(tableA);
//
//        //4.执行查询
//        System.out.println(tableA);
//        // union the two tables
//        Table resultTable = tEnv.sqlQuery(
//                "SELECT * FROM " + tableA + " WHERE amount > 2 "
//        );
//
//        //5.输出结果
//        DataStream<Order> resultDS = tEnv.toAppendStream(resultTable, Order.class);
//        resultDS.print();
//            env.execute();
//    }
}
