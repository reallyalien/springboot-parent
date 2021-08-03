package com.ot.springboot.kafka.chapter2;


import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

public class CompanySerializer implements Serializer<Company> {

    @Override
    public byte[] serialize(String topic, Company data){
        if (data == null ) return new byte[0];
        String company = JSON.toJSONString(data);
        byte[] bytes = new byte[0];
        try {
            bytes = company.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
