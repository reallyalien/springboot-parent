package com.ot.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Title: C
 * @Author wangtao
 * @Date 2023/12/20 11:17
 * @description:
 */
public class C {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        String hdfsAddr = "hdfs://node1001:8020/dolphinscheduler/root/resources/test-udf/flink-test-udf-1.0-SNAPSHOT.jar";
        FileSystem fileSystem = FileSystem.get(new URI(hdfsAddr), configuration, "root");

        boolean exists = fileSystem.exists(new Path("hdfs://node1001:8020/dolphinscheduler/root/resources/test-udf/flink-test-udf-1.0-SNAPSHOT.jar"));
        System.out.println(exists);

    }
}
