package com.ot.springbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private JobRegistry jobRegistry;
    @Autowired
    @Qualifier("job1")
    private Job job;
    @Autowired
    private Job jobDecider;
    @Autowired
    private Job parentJob1;
    @Autowired
    private Job listenerJob;
    @Autowired
    private Job dbJob;
    @Autowired
    private Job exceptionJob;
    @Autowired
    private Job errorDemo1Job;
    @Autowired
    private Job retryDemoJob1;
    @Autowired
    private Job skipDemoJob22;
    @Autowired
    private Job skipListenerJob;

    @RequestMapping(value = "/run/{msg1}", method = RequestMethod.GET)
    public String jobRun(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            System.out.println("main任务执行前:"+Thread.currentThread().getName());
            jobLauncher.run(job, jobParameters);//此处不会去新创建线程去执行,任务的执行始终在主线程当中
            System.out.println("main任务执行后:"+Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runDecider/{msg1}", method = RequestMethod.GET)
    public String jobRunDecider(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(jobDecider, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runFuzi/{msg1}", method = RequestMethod.GET)
    public String jobRunFuzi(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(parentJob1, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runListener/{msg1}", method = RequestMethod.GET)
    public String jobRunListener(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(listenerJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runDb/{msg1}", method = RequestMethod.GET)
    public String jobRunDb(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(dbJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runException/{msg1}", method = RequestMethod.GET)
    public String jobRunException(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(exceptionJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runErrorDemo1Job/{msg1}", method = RequestMethod.GET)
    public String jobErrorDemo1Job(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(errorDemo1Job, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runRetryDemoJob1/{msg1}", method = RequestMethod.GET)
    public String jobRetryDemoJob1(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(retryDemoJob1, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runSkipDemoJob22/{msg1}", method = RequestMethod.GET)
    public String jobSkipDemoJob22(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {

            jobLauncher.run(skipDemoJob22, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }

    @RequestMapping(value = "/runSkipListener/{msg1}", method = RequestMethod.GET)
    public String jobSkipListener(@PathVariable("msg1") Long msg1) {
        JobParameters jobParameters = new JobParametersBuilder().addLong("msg1", msg1)
                .toJobParameters();
        //启动任务并传参
        try {
            jobLauncher.run(skipListenerJob, jobParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "job run success";
    }
}
