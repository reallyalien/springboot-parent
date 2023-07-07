package com.ot.activiti;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
public class TestTask1 {


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String jobId = "c6efaeaa985429bcb1c8ab5be83b3e34";
        String aa = "job_name='234382b5_d469_3e05_844d_9df0cd750fc3'";
//        String bb = "job_id='92b3c5161d916f3df6042b4360e794e9'";
        String url = "http://hadoop01:9090/api/v1/query_range?query=flink_taskmanager_job_task_operator_chunjun_numRead{" + aa + "}&start=1682009241&end=1682095641&step=10&timeout=2000";
        URI uri = UriComponentsBuilder.fromHttpUrl(url).build().encode().toUri();
        Object forObject1 = restTemplate.getForObject(uri, Object.class);
        System.out.println();
    }
}
