package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.accumulators.LongCounter;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.configuration.PipelineOptionsInternal;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.junit.Test;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class FlinkTest2 {


    private static final String TEXT_DATA = "hello world hello java hello js";

    /**
     * 批处理api
     * 统计单词个数
     */
    @Test
    public void batchWordCount() throws Exception {
        //创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //读取文本文件
//        DataSource<String> source = env.readTextFile("E:\\project\\myproject\\springboot-parent\\springboot-flink\\src\\main\\resources\\input\\1.txt", StandardCharsets.UTF_8.name());
        DataSource<String> source = env.fromElements(TEXT_DATA);
        //转换格式
        FlatMapOperator<String, Tuple2<String, Long>> wordAndOne = source.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                String[] split = value.split(" ");
                for (String s : split) {
                    out.collect(Tuple2.of(s, 1L));
                }
            }
            //当 Lambda 表达式使用 Java 泛型的时候, 由于泛型擦除的存在, 需要显示的声明类型信息
        }).returns(Types.TUPLE(Types.STRING, Types.LONG));
        //分组
        UnsortedGrouping<Tuple2<String, Long>> tuple2UnsortedGrouping = wordAndOne.groupBy(0);
        //聚合
        AggregateOperator<Tuple2<String, Long>> sum = tuple2UnsortedGrouping.sum(1);
        //打印结果
        sum.print();
    }

    /**
     * 流式处理
     *
     * @throws Exception
     */
    @Test
    public void streamWordCount() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        env.setParallelism(2);
//        DataStreamSource<String> streamSource = env.fromElements(TEXT_DATA);
        DataStreamSource<String> streamSource = env.readTextFile("E:\\project\\myproject\\springboot-parent\\springboot-flink\\src\\main\\resources\\input\\1.txt");
        //SingleOutputStreamOperator 返回值是这个的代表一个算子操作
        SingleOutputStreamOperator<Tuple2<String, Long>> returns = streamSource
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String s, Collector<String> collector) throws Exception {
                        Arrays.stream(s.split(" ")).forEach(collector::collect);
                    }
                })
                .returns(Types.STRING)
                .map(word -> Tuple2.of(word, 1L))
                .returns(Types.TUPLE(Types.STRING, Types.LONG));
        KeyedStream<Tuple2<String, Long>, String> keyBy = returns.keyBy(t -> t.f0);
        SingleOutputStreamOperator<Tuple2<String, Long>> sum = keyBy.sum(1);
        sum.print();
        env.execute();
    }

    /**
     * 测试异常
     *
     * @throws Exception
     */
    @Test
    public void streamWordCount1() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        DataStreamSource<String> streamSource = env.fromElements(TEXT_DATA);
        SingleOutputStreamOperator<String> stringSingleOutputStreamOperator = streamSource.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String s, Collector<String> collector) throws Exception {
                Arrays.stream(s.split(" ")).forEach(collector::collect);
            }

        });
        KeyedStream<String, String> stringStringKeyedStream = stringSingleOutputStreamOperator.keyBy(t -> t);
        stringStringKeyedStream.print();
        env.execute();

    }

    /**
     * @throws Exception
     */
    @Test
    public void clickSourceTest() throws Exception {
        LocalStreamEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        DataStreamSource<Event> streamSource = env.addSource(new ClickSource()).setParallelism(1);
        SingleOutputStreamOperator<String> map = streamSource.map(e -> e.user);
        map.print();
        env.execute();
    }

    /**
     * 测试累加器
     *
     * @throws Exception
     */
    @Test
    public void streamWordCountAdd() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        DataStreamSource<String> streamSource = env.fromElements(TEXT_DATA);
        Timer timer = new Timer();
        LongCounter counter = new LongCounter();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(counter);
            }
        }, 0, 10);
        SingleOutputStreamOperator<Object> flatMap = streamSource.flatMap(new RichFlatMapFunction<String, Object>() {
            @Override
            public void flatMap(String value, Collector<Object> out) throws Exception {
                Arrays.stream(value.split(" ")).forEach(e -> {
                    out.collect(e);
                    counter.add(1);
                });
            }

            @Override
            public void open(Configuration parameters) throws Exception {
                RuntimeContext runtimeContext = getRuntimeContext();
                runtimeContext.addAccumulator("mt", counter);
            }
        });
        flatMap.print();
        env.execute();

    }

    /**
     * 测试累加器
     *
     * @throws Exception
     */
    @Test
    public void strea() throws Exception {
//        Configuration configuration = new Configuration();
//        configuration.setString(PipelineOptionsInternal.PIPELINE_FIXED_JOB_ID, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(configuration);
//        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
//        DataStreamSource<String> streamSource = env.fromElements(TEXT_DATA);
//
//        SingleOutputStreamOperator<Object> flatMap = streamSource.flatMap(new RichFlatMapFunction<String, Object>() {
//            @Override
//            public void flatMap(String value, Collector<Object> out) throws Exception {
//                Arrays.stream(value.split(" ")).forEach(e -> {
//                    out.collect(e);
//                });
//            }
//        });
//        flatMap.print();
//        JobExecutionResult result = env.execute();
//        JobID jobID = result.getJobID();
//        System.out.println(jobID);
        System.out.println("1".equalsIgnoreCase(null));

    }

}
