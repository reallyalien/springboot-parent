package com.ot.springbatch.job;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateText {


    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("d:/1.txt",true);
        String str="\t李四\tstudy,programmer,money\tschooladdr:西安,homeaddr:兰州\t2020-01-06";
        for (int i = 0; i < 10000; i++) {
            String s = i + str;
            fos.write(s.getBytes());
            fos.write("\n".getBytes());
        }
        fos.flush();
        fos.close();
    }
}
