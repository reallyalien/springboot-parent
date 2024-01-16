package com.ot.springboot.kafka.test;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Future;

@Slf4j
public class ProduceJSOn {
    private static final String brokerList = "192.168.2.122:6667";
    private static final String topicName = "realtime-dev-source-json-001";

    public static void main(String[] args) {
        Properties properties = new Properties();
        //设置kay的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        //设置value的序列化方式
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置集群地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        //创建生成者
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        try {
            for (long i = 1000; i <= 1200; i++) {
                Random r = new Random();
                String[] users = {"孙悟空", "猪八戒", "沙僧", "唐三藏", "杨戬", "哪吒", "如来", "观音"};
                String[] address = {"长安", "建康", "洛阳", "咸阳", "兖州", "荆州", "青州", "蓉城"};
                Integer[] ages = {28, 34, 17, 800, 45, 54, 78, 83, 110, 67};
                Float[] weights = {80.1F, 78.4F, 67.3F, 90.123F, 56.78F, 34.12F};
                Double[] heights = {178.06D, 200.01D, 180.78D, 197.85D, 169.62D};
                BigDecimal[] prices = {BigDecimal.ZERO, BigDecimal.ONE, new BigDecimal("23.11234"), new BigDecimal("5999.00"), new BigDecimal("79999.9999"), new BigDecimal("500.01")};
                Boolean[] audits = {false, true, true, false, false, true, true, true, false, false, true};
                User user = User.builder()
                        .userId(i)
                        .userName(users[r.nextInt(users.length)])
                        .address(address[r.nextInt(address.length)])
                        .birthday(DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss"))
                        .death(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
                        .longDeath(String.valueOf(System.currentTimeMillis()))
                        .age(ages[r.nextInt(ages.length)])
                        .weight(weights[r.nextInt(weights.length)])
                        .height(heights[r.nextInt(heights.length)])
                        .price(prices[r.nextInt(prices.length)])
                        .adult(audits[r.nextInt(audits.length)])
                        .build();
                if (i % 3 == 0) {
                    user.setWeight(null);
                }
                if (i % 5 == 0) {
                    user.setHeight(null);
                }
                if (i % 7 == 0) {
                    user.setAddress("");
                }
                String s = JSON.toJSONString(user, SerializerFeature.WriteMapNullValue);
                System.out.println(s);
                ProducerRecord<String, String> record = new ProducerRecord<>(topicName, s);
                Future<RecordMetadata> metadataFuture = producer.send(record);
                RecordMetadata recordMetadata = metadataFuture.get();
                System.out.println("topic:" + recordMetadata.topic() + "---" + "partition:" + recordMetadata.partition() + "----" + "offset:" + recordMetadata.offset());

                Thread.sleep(1000);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }
}
