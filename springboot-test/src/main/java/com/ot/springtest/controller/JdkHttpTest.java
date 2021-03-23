package com.ot.springtest.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JdkHttpTest {


    public static void main(String[] args) throws IOException {
        HttpURLConnection connection=null;
        URL url=new URL("http://localhost:8096/a");
        connection= (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        connection.setConnectTimeout(3 * 1000);
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");
    }
}
