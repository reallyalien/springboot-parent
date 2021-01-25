package com.ot.springboot.hdfs;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class NewWordCount {

    /**
     * MapReduceBase类:实现了Mapper和Reducer接口的基类（其中的方法只是实现接口，而未作任何事情）
     * Mapper接口：
     * WritableComparable接口：实现WritableComparable的类可以相互比较。所有被用作key的类应该实现此接口。
     * Reporter 则可用于报告整个应用的运行进度，本例中未使用。
     */
    static class MyMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        /**
         * 映射一个单个的输入k/v到一个中间的k/v对
         * 输出对 不需要和 输入对 是相同的数据类型，输入对可以映射到0个或多个 输出对
         * OutputCollector：收集mapper和reducer输出的k/v对
         * OutputCollector的collect(k,v)方法，增加一个k/v到output
         *
         * @param key
         * @param value
         * @param outputCollector
         * @param reporter
         * @throws IOException
         */
        @Override
        public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> outputCollector, Reporter reporter) throws IOException {
            String s = value.toString();
            StringTokenizer stringTokenizer = new StringTokenizer(s);
            while (stringTokenizer.hasMoreTokens()) {
                word.set(stringTokenizer.nextToken());
                outputCollector.collect(word, one);
            }
        }
    }

    static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                IntWritable intWritable = values.next();
                sum += intWritable.get();
            }
            output.collect(key, new IntWritable(sum));
        }
    }

    //==============================================Main方法测试=============================================================

    public static void main(String[] args) throws IOException {
        /**
         * jobConf:map/reduce的配置类，先hadoop框架描述map-reduce的工作
         */
        JobConf conf = new JobConf(NewWordCount.class);
        //自定义job名称
        conf.setJobName("job");
        //为job的输出数据设置key类型
        conf.setOutputKeyClass(Text.class);
        //为job的输出数据设置value类型
        conf.setOutputValueClass(IntWritable.class);
        //为job设置mapper类
        conf.setMapperClass(MyMap.class);
        //为job设置combine类
        conf.setCombinerClass(Reduce.class);
        //为job设置reduce类
        conf.setReducerClass(Reduce.class);
        //为map-reduce任务设置InputFormat实现类
        conf.setInputFormat(TextInputFormat.class);
        //为map-reduce任务设置OutputFormat实现类
        conf.setOutputFormat(TextOutputFormat.class);


        /**
         * InputFormat描述map-reduce中对job的输入定义
         * setInputPaths():为map-reduce job设置路径数组作为输入列表
         * setInputPath()：为map-reduce job设置路径数组作为输出列表
         */
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(args[1]));
        JobClient.runJob(conf);
    }
}
