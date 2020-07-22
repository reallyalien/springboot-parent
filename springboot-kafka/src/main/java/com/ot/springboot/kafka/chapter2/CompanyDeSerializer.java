package com.ot.springboot.kafka.chapter2;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

public class CompanyDeSerializer implements Deserializer<Company> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Company deserialize(String topic, byte[] data) {
        Company company=null;
        try {
            String s = new String(data, "utf-8");
            company = (Company) JSON.parse(s);
            System.out.println("/////");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public void close() {

    }
}
