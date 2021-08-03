package com.ot.springbatch.decider;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableBatchProcessing
public class DeciderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private MyDedicider myDedicider;

    @Bean
    public Job jobDecider() {
        return jobBuilderFactory.get("jobDecider")
                .start(stepDecider1())
                .next(myDedicider)//由决策器的返回值决定执行哪个step
                .from(myDedicider).on("even").to(stepDecider2())
                .from(myDedicider).on("odd").to(stepDecider3())
                .from(stepDecider3()).on("*").to(myDedicider)
                .end().build();
    }

    @Bean
    public Step stepDecider1() {
        return stepBuilderFactory.get("stepDecider1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("stepDecider1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step stepDecider2() {
        return stepBuilderFactory.get("stepDecider2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("stepDecider2");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step stepDecider3() {
        return stepBuilderFactory.get("stepDecider3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("stepDecider3");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    //定义一个决策器,根据决策器的值来决定使用哪个step
    @Component
    class MyDedicider implements JobExecutionDecider {

        private int count = 0;

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            count++;
            if (count % 2 == 0) {
                return new FlowExecutionStatus("even");
            } else {
                return new FlowExecutionStatus("odd");
            }
        }
    }
}
