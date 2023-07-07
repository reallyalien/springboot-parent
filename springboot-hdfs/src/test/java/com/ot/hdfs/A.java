package com.ot.hdfs;


import jdk.nashorn.internal.objects.annotations.Getter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;;
import org.apache.parquet.example.data.simple.SimpleGroup;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class A {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Configuration conf = new Configuration();
        conf.set("mapreduce.map.output.compress.codec", "SnappyCodec");
        conf.set("fs.defaultFS", "hdfs://192.168.2.121:8020");
        Path path = new Path("/warehouse/tablespace/managed/hive/hdfs/43a591aac73736be5899e7a74cc6ceeb_0_0.snappy");
        ParquetReader<Group> reader = ParquetReader.builder(new GroupReadSupport(), path).withConf(conf).build();
        Group group;
        int i = 0;
        List<String> result = new ArrayList<>();
        while ((group = reader.read()) != null) {
            if (i == 10) {
                break;
            }
            result.add(group.toString());
            i++;
        }
        reader.close();
    }
}
