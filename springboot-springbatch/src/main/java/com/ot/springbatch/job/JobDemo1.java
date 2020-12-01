package com.ot.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing
@Component
public class JobDemo1 {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean("job1")
    public Job job() {
        return jobBuilderFactory.get("job1")
                .start(step1())
                .next(step2())
                .build();

    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        for (int i = 0; i < 10; i++) {
                            System.out.println("step1[+" + i + "]" + Thread.currentThread().getName());
                            if (i==9) throw new RuntimeException("错误数据");
                        }
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        for (int i = 10; i < 20; i++) {
                            System.out.println("step2[+" + i + "]" + Thread.currentThread().getName());
//                            if (i==18) throw new RuntimeException("错误数据");
                            //下次重新运行只运行错误的step
                        }
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }


}
