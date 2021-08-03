package com.ot.springboot.script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ScriptTask {

    @Autowired
    public void executor() throws IOException {
        String cmd = "/bin/sh /root/d/1.sh";
        int exitValue = -1;

        BufferedReader bufferedReader = null;
        try {
            // command process
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));

            // command log
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            // command exit
            process.waitFor();
            exitValue = process.exitValue();
        } catch (Exception e) {

        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        if (exitValue == 0) {
            System.out.println("成功");
        } else {
            System.out.println("失败");
        }
    }
}
