package com.ot.springbatch.process;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SkipDemoJob {

    @Autowired
    private JobBuilderFactory jobBuilders;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Bean
    public Job skipDemoJob22() {
        return jobBuilders.get("skipDemoJob22")
                .start(skipDemoStep22())
                .build();
    }

    @Bean
    public Step skipDemoStep22() {
        return stepBuilders.get("skipDemoStep22")
                .<String, String>chunk(10)
                .reader(skipDemoRead22())
                .processor(skipDemoProcess22())
                .writer(skipDemoWriter22())
                .faultTolerant()
                .skip(CustomRetryException.class)//碰到什么异常跳过
                .skipLimit(5)//跳过几次
                .build();
    }

    @Bean
    public ItemWriter<String> skipDemoWriter22() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach((s)->{
                    System.out.println("write:"+s);
                });
            }
        };
    }

    @Bean
    public ItemProcessor<String, String> skipDemoProcess22() {
        return new ItemProcessor<String, String>() {

            private int attemptCount = 0;

            @Override
            public String process(String s) throws Exception {
                System.out.println("process:" + s);
                if (s.equalsIgnoreCase("26")) {
                    attemptCount++;
                    if (attemptCount >= 3) {
                        System.out.println("retry " + attemptCount + "\ttimes success");
                        return String.valueOf(Integer.valueOf(s) * -1);
                    } else {
                        System.out.println("process the" + "\t"+attemptCount + "\ttimes fail");
                        throw new CustomRetryException("process fail attemp" + attemptCount);
                    }
                } else {
                    return String.valueOf(Integer.valueOf(s) * -1);
                }
            }
        };
    }

    /**
     * @return
     * @ JobScope:在任务实例化的时候创建Bean,开启延迟Bean实例功能
     * @ StepScope:在step被使用时创建Bean,开启延迟加载功能
     */
    @Bean
    @StepScope
    public ListItemReader<String> skipDemoRead22() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(String.valueOf(i));
        }
        ListItemReader<String> reader = new ListItemReader<>(list);
        return reader;
    }

    class CustomRetryException extends Exception {

        public CustomRetryException() {
        }

        public CustomRetryException(String message) {
            super(message);
        }
    }
}