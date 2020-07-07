package com.ot.schedulebatch.task;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@EnableAsync
public class ScheduleTask {
    private static final String CRON = "* * * * * ?";//每秒执行一次

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;
    @Scheduled(cron = CRON)
    @Async
    public void test() throws Exception {
        long millis = System.currentTimeMillis();
        JobParameters jobParameters = new JobParametersBuilder().addLong("key", millis).toJobParameters();
        JobExecution run = jobLauncher.run(job, jobParameters);
        System.out.println("任务执行结束...");
    }
}
