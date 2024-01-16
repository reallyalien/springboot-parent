package com.ot.flink;

import com.alibaba.fastjson2.JSON;
import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import com.ot.flink.source.ClickSource2;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
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

/**
 * 时间窗口测试
 */
public class CountTableTest {


    /**
     * 动态数据 1
     */
    @Test
    public void watermark() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism();
        //获取表环境
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);
        DataStreamSource<Event> source1 = env.addSource(new ClickSource());
        //将数据流转换成表
        Table eventTable1 = tableEnvironment.fromDataStream(source1, $("user").as("u1"), $("timestamp").as("t1"), $("url").as("url1"));

        tableEnvironment.createTemporaryView("a", eventTable1);


        //执行sql
        String sql = "select count (u1) from a";
        Table result = tableEnvironment.sqlQuery(sql);


        //将表结果转换成stream输出
        DataStream<Row> rowDataStream = tableEnvironment.toChangelogStream(result);
        rowDataStream.print();
        env.execute();
    }

    /**
     * 动态数据 2
     */
    @Test
    public void watermark1() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism(1);

        DataStreamSource<Event> source1 = env.addSource(new ClickSource());
        DataStreamSource<Event> source2 = env.addSource(new ClickSource2());

        source1.coGroup(source2)
                .where(e -> e.user)
                .equalTo(e -> e.user)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10))).apply(new CoGroupFunction<Event, Event, Object>() {
            @Override
            public void coGroup(Iterable<Event> first, Iterable<Event> second, Collector<Object> out) throws Exception {
                out.collect(first + "==>" + second);
            }
        })
                .print();
        env.execute();
    }

    /**
     * 水位线
     */
    @Test
    public void watermark2() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //获取表环境
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);
        DataStreamSource<Event> source1 = env.fromElements(
                new Event("A", "./home", 1000L),
                new Event("B", "./cart", 1000L),
                new Event("C", "./prod?id=1", 5 * 1000L),
                new Event("D", "./home", 60 * 1000L),
                new Event("E", "./prod?id=3", 90 * 1000L),
                new Event("F", "./prod?id=7", 105 * 1000L),
                new Event("G", "./prod?id=7", 105 * 1000L),
                new Event("H", "./prod?id=7", 105 * 1000L),
                new Event("I", "./prod?id=7", 105 * 1000L),
                new Event("J", "./prod?id=3", 105 * 1000L),
                new Event("J", "./prod?id=3111111", 105 * 1000L),
                new Event("K", "./prod?id=7", 105 * 1000L)
        );
        DataStreamSource<Event> source2 = env.fromElements(
                new Event("J", "./home", 1000L),
                new Event("J", "./home222", 1000L),
                new Event("K", "./cart", 1000L),
                new Event("L", "./prod?id=1", 5 * 1000L),
                new Event("M", "./home", 60 * 1000L),
                new Event("N", "./prod?id=31", 90 * 1000L),
                new Event("O", "./prod?id=71", 105 * 1000L)
        );
        //将数据流转换成表
        Table eventTable1 = tableEnvironment.fromDataStream(source1, $("user").as("u"), $("timestamp").as("t"), $("url"));
        Table eventTable2 = tableEnvironment.fromDataStream(source2, $("user").as("u"), $("timestamp").as("t"), $("url"));

        tableEnvironment.createTemporaryView("a", eventTable1);
        tableEnvironment.createTemporaryView("b", eventTable2);
//        //执行sql
        String sql = "select a.u,b.u,a.url,a.t,b.url,b.t from a left join  b on a.u=b.u";
//        String sql = "select a.u from a";
        Table result = tableEnvironment.sqlQuery(sql);


        //将表结果转换成stream输出
        tableEnvironment.toChangelogStream(result).print();
//        tableEnvironment.toDataStream(result).print();
        env.execute();
//
//        Table url = eventTable.select($("url"));
//        tableEnvironment.toDataStream(url).print();
    }

    /**
     * 动态数据 1
     */
    @Test
    public void watermark3() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.setParallelism();
        //获取表环境
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);

        Event e1 = Event.builder().user("a").url("11").build();
        Event e2 = Event.builder().user("a").url("11").build();
        Map map1 = JSON.parseObject(JSON.toJSONString(e1), Map.class);
        DataStreamSource<Map> source = env.fromElements(map1);


        TypeInformation<Row> rowTypeInformation = BasicTypeInfo.of(Row.class);

        TypeInformation[] types = new TypeInformation[2];
        types[0] = BasicTypeInfo.STRING_TYPE_INFO;
        types[1] = BasicTypeInfo.STRING_TYPE_INFO;
        RowTypeInfo rowTypeInfo = new RowTypeInfo(types, new String[]{"name", "url"});
        SingleOutputStreamOperator<Row> process = source.process(new ProcessFunction<Map, Row>() {
            @Override
            public void processElement(Map value, Context ctx, Collector<Row> out) throws Exception {
                Row row = Row.of(value.get("user"), value.get("url"));
                out.collect(row);
            }
        }, rowTypeInfo);


//        Row a = Row.of("a", "10");
//        Row b = Row.of("a", "10");


//        DataStream<Row> source1 = env.fromElements(a,b);
//        Table table = tableEnvironment.fromDataStream(source, $("user"), $("timestamp"), $("url"));
        Table table = tableEnvironment.fromDataStream(process, $("name").as("name"), $("url").as("url"));


        tableEnvironment.toChangelogStream(table).print("aaaaaaaa");

        tableEnvironment.createTemporaryView("a", table);


        env.execute();
    }


    /**
     * 动态数据 1
     */
    @Test
    public void watermark4() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        DataStreamSource<Event> source = env.fromElements(
//                new Event("A", "./home", 1000L),
//                new Event("B", "./cart", 1000L),
//                new Event("C", "./cart", 1000L),
//                new Event("C", "", 1000L),
//                new Event("C", "./cart", 1000L)
//        );
        DataStreamSource<Event> source = env.addSource(new ClickSource());
        KeyedStream<Event, String> keyBy = source.keyBy(e -> e.user);
        SingleOutputStreamOperator<Event> aggregate = keyBy.window(TumblingProcessingTimeWindows.of(Time.seconds(5))).aggregate(new AggregateFunction<Event, Map<Event, Date>, Event>() {
            @Override
            public Map<Event, Date> createAccumulator() {
                return new HashMap<>();
            }

            @Override
            public Map<Event, Date> add(Event value, Map<Event, Date> accumulator) {
                accumulator.put(value, new Date());
                return accumulator;
            }

            @Override
            public Event getResult(Map<Event, Date> accumulator) {
                Event event = null;
                try {
                    Date date = simpleDateFormat.parse("1970-01-01 00:00:00");
                    for (Map.Entry<Event, Date> eventDateEntry : accumulator.entrySet()) {
                        if (eventDateEntry.getValue().compareTo(date) >= 0) {
                            event = eventDateEntry.getKey();
                        }
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("聚合：" + accumulator);
                System.out.println("结果：" + event);
                return event;
            }

            @Override
            public Map<Event, Date> merge(Map<Event, Date> a, Map<Event, Date> b) {
                return null;
            }
        });
        aggregate.print("xxxxxxx--->>>>>>>>>>");
        env.execute();
    }

    /**
     * 动态数据 1
     */
    @Test
    public void watermark5() throws Exception {
        Set<Map<String, Object>> maps = new HashSet<>();

        Map<String, Object> a = new HashMap<>();
        Map<String, Object> b = new HashMap<>();
        Map<String, Object> c = new HashMap<>();

        maps.add(a);
        maps.add(b);
        maps.add(c);
        System.out.println(maps);
    }


}
