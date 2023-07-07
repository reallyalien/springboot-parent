package com.ot.flink.function;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class MyFlatMap extends RichFlatMapFunction<String, Tuple2<String, Long>> {

    /**
     * 多次调用其实是线程每个线程调用的，每个线程只会调用一次
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        System.out.println(Thread.currentThread() + "open");
        super.open(parameters);
    }

    @Override
    public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
        out.collect(Tuple2.of(value, 1L));
    }

    @Override
    public void close() throws Exception {
        System.out.println(Thread.currentThread() + "close");
        super.close();
    }
}
