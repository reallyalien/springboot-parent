package com.ot.springboot.kafka.test;

import org.apache.kafka.common.protocol.types.Field;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class A {

    public static void main(String[] args) throws ParseException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String s = timestamp.toString();
        s = "2023/07/25 13:42:56";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date parse = simpleDateFormat2.parse(s);
        String format = simpleDateFormat1.format(parse);
        System.out.println(format);


        String a = "hello";
        String b = "el";


        String a1 = String.format("%s,%s,%s", "a", 1, "");
        System.out.println(a1);


        String a2 = ",,1,,3,,";
        String[] split = a2.split(",");
        System.out.println(split);
    }
}
