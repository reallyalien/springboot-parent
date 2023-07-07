package com.ot.flink;

import com.ot.flink.entity.Event;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.UUID;

public class TestFlink {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
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
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);
        //将数据流转换成表
        Table eventTable = tableEnvironment.fromDataStream(eventStream);
        //执行sql
        String sql = "select url, user from " + eventTable;
        Table result = tableEnvironment.sqlQuery(sql);
        //将表结果转换成stream输出
        tableEnvironment.toDataStream(result).print();
        JobExecutionResult execute = env.execute(args[0]);
        System.out.println(execute.getJobID());
        System.out.println(execute.toString());
    }
}
