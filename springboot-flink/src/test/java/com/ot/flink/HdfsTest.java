package com.ot.flink;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.FileSourceSplit;
import org.apache.flink.connector.file.src.impl.StreamFormatAdapter;
import org.apache.flink.connector.file.src.reader.BulkFormat;
import org.apache.flink.connector.file.src.reader.FileRecordFormat;
import org.apache.flink.connector.file.src.reader.TextLineFormat;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.parquet.ParquetInputFormat;
import org.apache.flink.formats.parquet.ParquetMapInputFormat;
import org.apache.flink.formats.parquet.ParquetRowInputFormat;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.source.FileProcessingMode;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Type;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;

public class HdfsTest extends BaseTest {


    @Test
    public void test1() {
        env.setParallelism(1);
        DataStreamSource<String> streamSource = env.readTextFile("hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/hdfs/0a0b033e05571db28b4b569379673d5b_0_0.snappy");
        streamSource.print();
    }

    @Test
    public void test2() {
        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        String dataPath = "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/hdfs/0a0b033e05571db28b4b569379673d5b_0_0.snappy";
        tableEnv.executeSql("CREATE TABLE `user_profile_data`(\n" +
                "  `device_id` STRING,\n" +
                "  `last_week_click_num` INT ," +
                "  `last_month_click_num` INT ," +
                "  `last_week_searchpv` INT  ," +
                " `year` string, " +
                " `month` string, " +
                " `day` string " +
                ") partitioned by(`year`,`month`,`day`)" +
                " WITH ( " +
                "  'connector' = 'filesystem',\n" +
                "  'path' = " + "'" + dataPath + "',\n" +
                "  'format' = 'parquet'\n" +
                "  )");
        String sql = "select * from user_profile_data";
        Table table = tableEnv.sqlQuery(sql);
        tableEnv.toDataStream(table).print();
    }

    @Test
    public void test3text() {
        env.setParallelism(1);
        String dataPath = "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/flink/2023-06-26--14/.part-0-0.inprogress.f6817778-bca6-4868-a5b9-4fc57d9f3521";
        Path path = new Path(dataPath);
        TextInputFormat textInputFormat = new TextInputFormat(path);
        DataStreamSource<String> stringDataStreamSource = env.readFile(textInputFormat, dataPath);
        SingleOutputStreamOperator<Map> map = stringDataStreamSource.map(new MapFunction<String, Map>() {
            @Override
            public Map map(String value) throws Exception {
                System.out.println(value);
                return JSON.parseObject(value, Map.class);
            }
        });
        map.print();
    }

    @Test
    public void test3parquet() {
        env.setParallelism(1);
//        String dataPath = "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/hdfs/1168cbc875e68b859eeebcdbdc7b9b44_0_0.snappy";
        String dataPath = "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/hdfs";
        Path path = new Path(dataPath);
        final ArrayList<Type> cityFields = new ArrayList<>();
        cityFields.add(new PrimitiveType(Type.Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.INT64, "id"));
        cityFields.add(new PrimitiveType(Type.Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.BINARY, "name"));
        ParquetMapInputFormat parquetMapInputFormat = new ParquetMapInputFormat(path, new MessageType("", cityFields));
        DataStreamSource<Map> file = env.readFile(parquetMapInputFormat, dataPath);
//        ParquetRowInputFormat parquetRowInputFormat = new ParquetRowInputFormat(path, new MessageType("", cityFields));
//        DataStreamSource<Row> file = env.readFile(parquetRowInputFormat, dataPath);
        file.print();
    }

    @Test
    public void getParquetMeta() throws IOException {
        env.setParallelism(1);
        String dataPath = "hdfs://192.168.2.121:8020/flink";
        Path path = new Path(dataPath);
        org.apache.hadoop.fs.Path hadoopPath = new org.apache.hadoop.fs.Path(dataPath);
//        final ArrayList<Type> cityFields = new ArrayList<>();
//        cityFields.add(new PrimitiveType(Type.Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.INT64, "id"));
//        cityFields.add(new PrimitiveType(Type.Repetition.REQUIRED, PrimitiveType.PrimitiveTypeName.BINARY, "name"));
//        ParquetMapInputFormat parquetMapInputFormat = new ParquetMapInputFormat(path, new MessageType("", cityFields));
//        DataStreamSource<Map> file = env.readFile(parquetMapInputFormat, dataPath);
////        ParquetRowInputFormat parquetRowInputFormat = new ParquetRowInputFormat(path, new MessageType("", cityFields));
////        DataStreamSource<Row> file = env.readFile(parquetRowInputFormat, dataPath);
//        file.print();

        GroupReadSupport groupReadSupport = new GroupReadSupport();
        ParquetReader<Group> build = ParquetReader.builder(groupReadSupport, hadoopPath).build();
        //3.读取一个数据，并输出group的schema列名
        Group group = null;
        if (((group = build.read()) != null)) {
            int fieldCount = group.getType().getFieldCount();
            for (int field = 0; field < fieldCount; field++) {
                Type fieldType = group.getType().getType(field);
                String fieldName = fieldType.getName();
                System.out.println(fieldType + "--" + fieldName);
            }
        }

    }


    @Test
    public void test4() {
        env.setParallelism(1);
        String dataPath = "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/flink/2023-06-26--14/.part-0-0.inprogress.f6817778-bca6-4868-a5b9-4fc57d9f3521";
        Path path = new Path(dataPath);
        FileSource<String> build = FileSource.forRecordStreamFormat(new TextLineFormat(), path).build();
        DataStreamSource<String> source = env.fromSource(build, WatermarkStrategy.noWatermarks(), "xx");
        source.print();
    }

    @Test
    public void test5() {
        env.setParallelism(1);
        String dataPath = "hdfs://192.168.2.121:8020/warehouse/tablespace/managed/hive/flink/2023-06-26--14/.part-0-0.inprogress.f6817778-bca6-4868-a5b9-4fc57d9f3521";
        Path path = new Path(dataPath);
        FileSource<String> build = FileSource.forBulkFileFormat(new StreamFormatAdapter<>(new TextLineFormat()), path).build();
        DataStreamSource<String> source = env.fromSource(build, WatermarkStrategy.noWatermarks(), "xx");
        source.print();
    }
}
