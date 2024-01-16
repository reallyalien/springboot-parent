package com.ot.flink;

import com.ot.flink.entity.Event;
import org.apache.flink.api.common.io.RichInputFormat;
import org.apache.flink.core.io.InputSplit;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.InputFormatSourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.table.api.*;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.data.RowData;
import org.apache.hadoop.conf.Configuration;
import org.apache.iceberg.flink.FlinkCatalog;
import org.apache.iceberg.flink.TableLoader;
import org.apache.iceberg.flink.source.FlinkSource;
import org.junit.Test;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.rowInterval;

/**
 * table api 和sql 测试
 */
public class TableApiTest extends BaseTest {


    @Test
    public void table() {
        env.setParallelism(1);
        //获取表环境
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);
        SingleOutputStreamOperator<Event> eventStream = env
                .fromElements(
                        new Event("Alice", "./home", 1000L),
                        new Event("Bob", "./cart", 1000L),
                        new Event("Alice", "./prod?id=1", 5 * 1000L),
                        new Event("Cary", "./home", 60 * 1000L),
                        new Event("Bob", "./prod?id=3", 90 * 1000L),
                        new Event("Alice", "./prod?id=7", 105 * 1000L)
                );
        //将数据流转换成表
        Table eventTable = tableEnvironment.fromDataStream(eventStream);
//        //执行sql
        String sql = "select url, user from " + eventTable;
        Table result = tableEnvironment.sqlQuery(sql);
        //将表结果转换成stream输出
        tableEnvironment.toDataStream(result).print();

        Table url = eventTable.select($("url"));
        tableEnvironment.toDataStream(url).print();
    }

    /**
     * 同样还是用户的
     * 一组点击事件，我们可以查询出某个用户（例如 Alice）点击的 url 列表，也可以统计出每个
     * 用户累计的点击次数，这可以用两句 SQL 来分别实现。
     */
    @Test
    public void sql() {
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> eventStream = env
                .fromElements(
                        new Event("Alice", "./home", 1000L),
                        new Event("Bob", "./cart", 1000L),
                        new Event("Alice", "./prod?id=1", 5 * 1000L),
                        new Event("Cary", "./home", 60 * 1000L),
                        new Event("Bob", "./prod?id=3", 90 * 1000L),
                        new Event("Alice", "./prod?id=7", 105 * 1000L)
                );
        //获取表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //将数据流转换成表
        tableEnv.createTemporaryView("eventTable", eventStream);
        //查询Alice的访问url
        Table aliceVisitTable = tableEnv.sqlQuery("select url,user from eventTable where user = 'Alice'");
        //统计每个用户的点击次数
        Table urlCountTable = tableEnv.sqlQuery("SELECT user, COUNT(url) FROM eventTable GROUP BY user");
        //将表转换成流输出
        tableEnv.toDataStream(aliceVisitTable).print("alice visit");
        tableEnv.toChangelogStream(urlCountTable).print("count");
    }

    /**
     * 动态表
     */
    @Test
    public void dynamicTable() {
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> eventStream = env
                .fromElements(
                        new Event("Alice", "./home", 1000L),
                        new Event("Bob", "./cart", 1000L),
                        new Event("Alice", "./prod?id=1", 5 * 1000L),
                        new Event("Cary", "./home", 60 * 1000L),
                        new Event("Bob", "./prod?id=3", 90 * 1000L),
                        new Event("Alice", "./prod?id=7", 105 * 1000L)
                );
        //获取表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //将数据流转换成表
        tableEnv.createTemporaryView("eventTable", eventStream, $("user"), $("url"), $("timestamp").as("ts"));

        //统计每个用户的点击次数
        //随着数据的不断到了，这个urlCountTable动态表的数据会不断新增和更新，因此转换成流就需要以更新日志的方式去输出
        Table urlCountTable = tableEnv.sqlQuery("SELECT user, COUNT(url) FROM eventTable GROUP BY user");
        //将表转换成流输出
        tableEnv.toChangelogStream(urlCountTable).print("count");
    }

    /**
     * 动态表
     */
    @Test
    public void kafka() {
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> eventStream = env
                .fromElements(
                        new Event("Alice", "./home", 1000L),
                        new Event("Bob", "./cart", 1000L),
                        new Event("Alice", "./prod?id=1", 5 * 1000L),
                        new Event("Cary", "./home", 60 * 1000L),
                        new Event("Bob", "./prod?id=3", 90 * 1000L),
                        new Event("Alice", "./prod?id=7", 105 * 1000L)
                );
        //获取表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //将数据流转换成表
        tableEnv.createTemporaryView("eventTable", eventStream, $("user"), $("url"), $("timestamp").as("ts"));

        Table result = tableEnv.sqlQuery("select user,url from eventTable");

        //创建kafka连接器
        String createKafkaSql = "CREATE TABLE kafkaTable (\n" +
                "`user` STRING,\n" +
                " `url` STRING\n" +
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'events',\n" +
                " 'properties.bootstrap.servers' = '192.168.197.130:9092',\n" +
                " 'properties.group.id' = 'testGroup',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n" +
                " 'format' = 'csv'\n" +
                ")";

        //创建表
        TableResult tableResult = tableEnv.executeSql(createKafkaSql);
        System.out.println(tableResult);

        //向这个表插入数据
        result.executeInsert("kafkaTable");
//        tableEnv.toDataStream(result).print();
    }

    /**
     * 动态表
     */
    @Test
    public void kafka2() throws Exception {
        TableLoader tableLoader = TableLoader.fromHadoopTable(
                "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/iceberg_test.db/audit_project1",
                new Configuration()
        );
        DataStream<RowData> batch = FlinkSource.forRowData().env(env).tableLoader(tableLoader).streaming(false).build();
        batch.print();
        env.execute();
    }
}
