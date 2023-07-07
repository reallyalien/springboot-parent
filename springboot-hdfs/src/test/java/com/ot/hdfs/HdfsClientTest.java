package com.ot.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HdfsClientTest {

    FileSystem fs = null;

    @Before
    public void before() throws IOException, URISyntaxException, InterruptedException {
        Configuration configuration = new Configuration();
        String hdfsAddr = "hdfs://hadoop01:9820";
        fs = FileSystem.get(new URI(hdfsAddr), configuration, "root");
    }

    @After
    public void after() throws IOException, URISyntaxException, InterruptedException {
        fs.close();
    }

    @Test
    public void testConn() throws IOException, URISyntaxException, InterruptedException {
        String url = "hdfs://hadoop01:9820";
        String user = "root";
        boolean b = HDFSUtil.testConnection(url, user);
        System.out.println(b);
    }

    @Test
    public void testConn1() throws IOException, URISyntaxException, InterruptedException {
        String url = "hdfs://hadoop01:9820";
        String user = "root";
        String path = "/mysqldb/test1/.data/fa15301d77bccd2464118f445865ffdb_0_0.snappy";
        String s = url + path;
//        HDFSUtil.getHDFSData(s,fs);
        HDFSUtil.decompres(s,fs);
//        HDFSUtil.readFromSequenceFile(s);
        System.out.println();

    }

    @Test
    public void testAppend() throws IOException, URISyntaxException, InterruptedException {
        String url = "hdfs://192.168.2.121:8020";
        String user = "root";
        String path = "/flink/1.txt/.data/2506c227f7de4ffbb9021afc5f4082d9_0_0";
        String s = url + path;
        HDFSUtil.append(s);

    }
}
