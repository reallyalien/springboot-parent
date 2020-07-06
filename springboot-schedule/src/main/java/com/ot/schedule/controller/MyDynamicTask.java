package com.ot.schedule.controller;

import com.ot.schedule.task.ScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MyDynamicTask {

    @Autowired
    private ScheduleTask scheduleTask;


    @GetMapping
    public void getCron(String time, String name, Integer task) {
        if (task == 1) {
            scheduleTask.setCron(time);
            scheduleTask.setName(name);
        }
    }
}
