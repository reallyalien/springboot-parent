package com.ot.springboot.kafka.chapter7;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;

public class ProducerTransaction {

    //kafka集群地址
    private static final String brokerList = "localhost:9092";
    //topic
    private static final String topicName = "test-kafka-1";
    //事务
    private static final String transactionId = UUID.randomUUID().toString();

    public static void main(String[] args) {

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(init());
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, "111");
        try {
            //初始化事务
            producer.initTransactions();
            //开启事务
            producer.beginTransaction();

            producer.send(record);
            //提交事务
            producer.commitTransaction();
        } catch (Exception e) {
            //回滚事务
            producer.abortTransaction();
        } finally {
            producer.close();
        }
    }

    public static Properties init() {
        Properties prop = new Properties();
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.RETRIES_CONFIG, 3);
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //设置幂等性，只能保证 Producer 在单个会话内不丟不重，如果 Producer 出现意外挂掉再重启是无法保证的
        //(幂等性情况下，是无法获取之前的状态信息，因此是无法做到跨会话级别的不丢不重）;
        //幂等性不能跨多个 Topic-Partition，只能保证单个 partition 内的幂等性，当涉及多个 Topic，Partition 时，这中间的状态并没有同步。
        //英 [ˈaɪdəmpəʊtəns]
        prop.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        //设置事务id，幂等性不能跨区操作，但是事务可以保证对多个分区的写入操作的原子性
        //事务要求生产者开启幂等性特性（不开启会抛异常），将transactional.id设置非空，开启事务
        prop.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionId);
        return prop;
    }
}
