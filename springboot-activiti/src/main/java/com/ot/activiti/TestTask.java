package com.ot.activiti;

import org.springframework.web.client.RestTemplate;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TestTask {


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String jobId = "c6efaeaa985429bcb1c8ab5be83b3e34";


        Map forObject = restTemplate.getForObject("http://192.168.197.101:8081/jobs/" + jobId + "/accumulators", Map.class);
        List<Map<String, Object>> list = (List<Map<String, Object>>) forObject.get("user-task-accumulators");
        list.stream().filter(e -> e.get("name").equals("numWrite") || e.get("name").equals("numRead")).forEach(e -> {
            Object value = e.get("value");
            System.out.println(e.get("name") + ":" + value);
        });
        System.out.println("====================");


    }
}
