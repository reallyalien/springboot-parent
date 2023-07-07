package com.ot.hdfs;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.example.GroupReadSupport;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

;

public class B {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Configuration conf = new Configuration();
        conf.set("mapreduce.map.output.compress.codec", "SnappyCodec");
        conf.set("fs.defaultFS", "hdfs://192.168.2.121:8020");
        Path path = new Path("/flink/e5be299a989e494f727732e670d928c5_0_0.gz");
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
        for (String s : result) {
            System.out.println(s);
        }
    }
}
