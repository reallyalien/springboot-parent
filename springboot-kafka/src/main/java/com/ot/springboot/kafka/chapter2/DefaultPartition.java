package com.ot.springboot.kafka.chapter2;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultPartition implements Partitioner {

    private final AtomicInteger counter=new AtomicInteger(0);
    //自定义分区策略
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //获取当前主题的所有分区
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int size = partitions.size();
        if (keyBytes == null){//如果没有设置key的值
            return counter.getAndIncrement() % size;//
        }else {
            //如果设置了key的值，则用雪花算法去计算分区
            int i = Utils.toPositive(Utils.murmur2(keyBytes)) % size;
            return i;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
