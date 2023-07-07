package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * 物理分区
 */
public class PhysicalPartitioningTest {


    StreamExecutionEnvironment env = null;

    @Before
    public void before() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
    }

    @After
    public void after() throws Exception {
        env.execute();
    }


    /**
     * 随机分区
     */
    @Test
    public void shuffle() {
        env.setParallelism(1);
        DataStreamSource<Event> dataStreamSource = env.addSource(new ClickSource());
        dataStreamSource.shuffle().print("shuffle").setParallelism(4);
    }

    /**
     * 轮询分区
     */
    @Test
    public void rebalance() {
        env.setParallelism(1);
        DataStreamSource<Event> dataStreamSource = env.addSource(new ClickSource());
        dataStreamSource.rebalance().print("shuffle").setParallelism(4);
    }

    /**
     * 重缩放分区
     */
    @Test
    public void rescale() {
        env.setParallelism(1);
        env.addSource(new RichParallelSourceFunction<Integer>() {
            @Override
            public void run(SourceContext<Integer> ctx) throws Exception {
                for (int i = 0; i < 8; i++) {
                    if ((i + 1) % 2 == getRuntimeContext().getIndexOfThisSubtask()) {
                        ctx.collect(i + 1);
                    }
                }
            }

            @Override
            public void cancel() {

            }
        })
                .setParallelism(2)
                .rebalance()
                .print()
                .setParallelism(4);
    }

    /**
     * 广播分区
     */
    @Test
    public void broadcast() {
        env.setParallelism(1);
        env.addSource(new ClickSource())
                .broadcast()
                .print()
                .setParallelism(4);
    }

    /**
     * 自定义分区
     * 对一组自然数按照奇偶性进行重分区
     */
    @Test
    public void customerPartition() {
        env.setParallelism(1);
        env.fromElements(1, 2, 3, 4, 5, 6, 7, 8).partitionCustom(new Partitioner<Integer>() {
            @Override
            public int partition(Integer key, int numPartitions) {
                return key % 2;
            }
        }, new KeySelector<Integer, Integer>() {
            @Override
            public Integer getKey(Integer value) throws Exception {
                return value;
            }
        }).print().setParallelism(2);
    }
}
