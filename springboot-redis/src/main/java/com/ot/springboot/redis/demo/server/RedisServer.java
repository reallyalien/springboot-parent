package com.ot.springboot.redis.demo.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RedisServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6379);
            Socket socket = serverSocket.accept();
            //接收消息
            byte[] bytes = new byte[2048];
            InputStream is;
            OutputStream os;
            is = socket.getInputStream();
            int len = is.read(bytes);
            System.out.println(new String(bytes, 0, len));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
