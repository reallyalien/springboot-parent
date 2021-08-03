package com.ot.springboot.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class A {

    public static void main(String[] args) throws IOException {
        String path = "a/1.py";
        FileOutputStream fos = new FileOutputStream(path);
        fos.write("hello".getBytes());
        fos.flush();
        fos.close();
    }
}
