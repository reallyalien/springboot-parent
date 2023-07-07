//package com.ot.flink.source;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.flink.streaming.api.functions.source.SourceFunction;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.fs.FileSystem;
//import org.apache.hadoop.fs.Path;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.net.URI;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//public class HdfsSource implements SourceFunction<String> {
//    @Override
//    public void run(SourceContext<String> ctx) throws Exception {
//        Configuration configuration = new Configuration();
//        String hdfsAddr = "hdfs://hadoop01:9820";
//        FileSystem fs = FileSystem.get(new URI(hdfsAddr), configuration, "root");
//        String targetPath = "d:/2.txt";
//        try {
//            fs.copyToLocalFile(false, new Path("/sanguo/shuguo/1.txt"), new Path(targetPath));
//            fs.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        List<String> strings = IOUtils.readLines(new FileInputStream(targetPath), StandardCharsets.UTF_8);
//        for (String string : strings) {
//            ctx.collect(string);
//        }
//    }
//
//    @Override
//    public void cancel() {
//
//    }
//}
