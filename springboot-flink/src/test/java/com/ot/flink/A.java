package com.ot.flink;

import com.alibaba.fastjson2.JSON;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple20;
import org.apache.flink.table.data.RowData;
import org.apache.flink.types.Row;
import org.apache.kafka.common.protocol.types.Field;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class A {

    public void a() {

    }

    private void b() {

    }

    public static void main(String[] args) {
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Date date = new Date();
//        Map<String, Object> map = new HashMap<>();
//        map.put("t1",timestamp);
//        map.put("t2",date);
//        String s = JSON.toJSONString(map);
//        Map map1 = JSON.parseObject(s, Map.class);
//        System.out.println(map1);


//        Date date = new Date(1686908136L*1000);
//        System.out.println(date);


//        String s="5,小二,1";
//        String[] split = s.split(",");
//        System.out.println(split);

//        BigDecimal a = new BigDecimal("3");
//        BigDecimal b = new BigDecimal("2");
//        String point = new String("\\\\");
//        String str = "1\\b";
//        String[] split = str.split(point);
//        System.out.println(Arrays.toString(split));
////        int i = a.compareTo(b);
////        System.out.println(i);
//
//        System.out.println(A.class.getName());


        Map<String,Object> map=new HashMap<>();

        Row row = new Row(10);
        row.setField("aaa","bbb");
        Object aaa = row.getField("aaa");
        System.out.println(row);

    }
}
