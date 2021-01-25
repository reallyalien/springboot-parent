package com.ot.springboot.map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * 统计气温变化
 * 比如：2010 0123 25表示在2010年01月23日的气温为25度。现在要求使用MapReduce，计算每一年出现过的最大气温。
 */
public class Temperature {

    /**
     * 四个泛型类型分别代表：
     * KeyIn        Mapper的输入数据的Key，这里是每行文字的起始位置（0,11,...）
     * ValueIn      Mapper的输入数据的Value，这里是每行文字
     * KeyOut       Mapper的输出数据的Key，这里是每行文字中的“年份”
     * ValueOut     Mapper的输出数据的Value，这里是每行文字中的“气温”
     * <p>
     * 每一行数据都会mapper，故而产生多个键值对，然后对键相同的进行分区，然后排序，有reduce调用reduce，没有则写入到hdfs文件当中
     */
    static class TempMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

        @Override
        public void map(LongWritable key,
                        Text value,
                        OutputCollector<Text, IntWritable> output,
                        Reporter reporter) throws IOException {
            System.out.println("mapper 之前：" + key + "," + value);
            String line = value.toString();
            String year = line.substring(0, 4);
            int temperature = Integer.parseInt(line.substring(8));
            output.collect(new Text(year), new IntWritable(temperature));
            System.out.println("mapper 输出：" + year + "," + temperature);
        }
    }

    /**
     * reduce会复制多个mapper的输出，把复制到reduce的本地数据进行合并，把分散的数据合成一个大数据，然后排序，调用reduce方法，再将
     * 结果的键值对写入到hdfs文件
     * 四个泛型类型分别代表：
     * KeyIn        Reducer的输入数据的Key，这里是每行文字中的“年份”
     * ValueIn      Reducer的输入数据的Value，这里是每行文字中的“气温”
     * KeyOut       Reducer的输出数据的Key，这里是不重复的“年份”
     * ValueOut     Reducer的输出数据的Value，这里是这一年中的“最高气温”
     */
    static class TempReduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {

        /**
         * 数据样本：2020,12,34,23,09
         *
         * @param key
         * @param values
         * @param output
         * @param reporter
         * @throws IOException
         */
        @Override
        public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
            StringBuilder sb = new StringBuilder();
            int maxValue = Integer.MIN_VALUE;
            while (values.hasNext()) {
                int i = values.next().get();
                maxValue = Math.max(maxValue, i);
                sb.append(i).append(",");
            }
            System.out.println("reduce 之前:" + key + "," + sb.toString());
            output.collect(key, new IntWritable(maxValue));
            System.out.println("reduce 之后:" + key + "," + maxValue);
        }
    }

    public static void main(String[] args) throws IOException {
        //输入路径
        String dst = "hdfs://idata190:9000/input.txt";
        //输出路径,必须是不存在的，空文件夹也不行
        String out = "hdfs://idata190:9000/out.txt";
        Configuration hadoopConfig = new Configuration();
        hadoopConfig.set("fs.hdfs.impl",
                org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        hadoopConfig.set("fs.file.impl",
                org.apache.hadoop.fs.LocalFileSystem.class.getName());
        JobConf conf = new JobConf(hadoopConfig, Temperature.class);
        //自定义job名称
        conf.setJobName("job");
        //为job的输出数据设置key类型
        conf.setOutputKeyClass(Text.class);
        //为job的输出数据设置value类型
        conf.setOutputValueClass(IntWritable.class);
        //为job设置mapper类
        conf.setMapperClass(Temperature.TempMapper.class);
        //为job设置combine类
        conf.setCombinerClass(Temperature.TempReduce.class);
        //为job设置reduce类
        conf.setReducerClass(Temperature.TempReduce.class);
        //为map-reduce任务设置InputFormat实现类
        conf.setInputFormat(TextInputFormat.class);
        //为map-reduce任务设置OutputFormat实现类
        conf.setOutputFormat(TextOutputFormat.class);
    }
    /*
2014010114
2014010216
2014010317
2014010410
2014010506
2012010609
2012010732
2012010812
2012010919
2012011023
2001010116
2001010212
2001010310
2001010411
2001010529
2013010619
2013010722
2013010812
2013010929
2013011023
2008010105
2008010216
2008010337
2008010414
2008010516
2007010619
2007010712
2007010812
2007010999
2007011023
2010010114
2010010216
2010010317
2010010410
2010010506
2015010649
2015010722
2015010812
2015010999
2015011023
     */
    //========================输出结果===================================

    /*
Before Mapper: 0, 2014010114======After Mapper:2014, 14
Before Mapper: 11, 2014010216======After Mapper:2014, 16
Before Mapper: 22, 2014010317======After Mapper:2014, 17
Before Mapper: 33, 2014010410======After Mapper:2014, 10
Before Mapper: 44, 2014010506======After Mapper:2014, 6
Before Mapper: 55, 2012010609======After Mapper:2012, 9
Before Mapper: 66, 2012010732======After Mapper:2012, 32
Before Mapper: 77, 2012010812======After Mapper:2012, 12
Before Mapper: 88, 2012010919======After Mapper:2012, 19
Before Mapper: 99, 2012011023======After Mapper:2012, 23
Before Mapper: 110, 2001010116======After Mapper:2001, 16
Before Mapper: 121, 2001010212======After Mapper:2001, 12
Before Mapper: 132, 2001010310======After Mapper:2001, 10
Before Mapper: 143, 2001010411======After Mapper:2001, 11
Before Mapper: 154, 2001010529======After Mapper:2001, 29
Before Mapper: 165, 2013010619======After Mapper:2013, 19
Before Mapper: 176, 2013010722======After Mapper:2013, 22
Before Mapper: 187, 2013010812======After Mapper:2013, 12
Before Mapper: 198, 2013010929======After Mapper:2013, 29
Before Mapper: 209, 2013011023======After Mapper:2013, 23
Before Mapper: 220, 2008010105======After Mapper:2008, 5
Before Mapper: 231, 2008010216======After Mapper:2008, 16
Before Mapper: 242, 2008010337======After Mapper:2008, 37
Before Mapper: 253, 2008010414======After Mapper:2008, 14
Before Mapper: 264, 2008010516======After Mapper:2008, 16
Before Mapper: 275, 2007010619======After Mapper:2007, 19
Before Mapper: 286, 2007010712======After Mapper:2007, 12
Before Mapper: 297, 2007010812======After Mapper:2007, 12
Before Mapper: 308, 2007010999======After Mapper:2007, 99
Before Mapper: 319, 2007011023======After Mapper:2007, 23
Before Mapper: 330, 2010010114======After Mapper:2010, 14
Before Mapper: 341, 2010010216======After Mapper:2010, 16
Before Mapper: 352, 2010010317======After Mapper:2010, 17
Before Mapper: 363, 2010010410======After Mapper:2010, 10
Before Mapper: 374, 2010010506======After Mapper:2010, 6
Before Mapper: 385, 2015010649======After Mapper:2015, 49
Before Mapper: 396, 2015010722======After Mapper:2015, 22
Before Mapper: 407, 2015010812======After Mapper:2015, 12
Before Mapper: 418, 2015010999======After Mapper:2015, 99
Before Mapper: 429, 2015011023======After Mapper:2015, 23


Before Reduce: 2001, 12, 10, 11, 29, 16, ======After Reduce: 2001, 29
Before Reduce: 2007, 23, 19, 12, 12, 99, ======After Reduce: 2007, 99
Before Reduce: 2008, 16, 14, 37, 16, 5, ======After Reduce: 2008, 37
Before Reduce: 2010, 10, 6, 14, 16, 17, ======After Reduce: 2010, 17
Before Reduce: 2012, 19, 12, 32, 9, 23, ======After Reduce: 2012, 32
Before Reduce: 2013, 23, 29, 12, 22, 19, ======After Reduce: 2013, 29
Before Reduce: 2014, 14, 6, 10, 17, 16, ======After Reduce: 2014, 17
Before Reduce: 2015, 23, 49, 22, 12, 99, ======After Reduce: 2015, 99
     */

    /*
Mapper的输入数据(k1,v1)格式是：默认的按行分的键值对<0, 2010012325>，<11, 2012010123>...
Reducer的输入数据格式是：把相同的键合并后的键值对：<2001, [12, 32, 25...]>，<2007, [20, 34, 30...]>...
Reducer的输出数(k3,v3)据格式是：经自己在Reducer中写出的格式：<2001, 32>，<2007, 34>...
     */
}
