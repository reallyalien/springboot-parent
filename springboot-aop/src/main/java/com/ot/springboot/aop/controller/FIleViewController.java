package com.ot.springboot.aop.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * 文件预览
 */
@RestController
@CrossOrigin
@RequestMapping("/file")
public class FIleViewController {


    @GetMapping("/view")
    public void view(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String filepath = "d:/CHECK_PROCESS_123123.docx";
//        String filepath = "d:/1.docx";
        FileInputStream inputStream = new FileInputStream(new File(filepath));
        byte[] bytes = new byte[1024];
        ServletOutputStream outputStream = response.getOutputStream();
        int len = 0;
        while ((len = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, len);
        }
        outputStream.close();
    }
}
