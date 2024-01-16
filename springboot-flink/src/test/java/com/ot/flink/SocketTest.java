package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;

/**
 * @Title: SocketTest
 * @Author wangtao
 * @Date 2023/8/14 10:49
 * @description:
 */
public class SocketTest {

    @Test
    public void test1() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        StreamTableEnvironment tableEnvironment = StreamTableEnvironment.create(env);
        SingleOutputStreamOperator<String> sensorDS = env
                .socketTextStream("192.168.2.111", 7777);
        Table table = tableEnvironment.fromDataStream(sensorDS);
        tableEnvironment.toDataStream(table).print();
        env.execute();
    }
}
