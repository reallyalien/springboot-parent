package com.ot.springbatch.process;


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

import java.util.Map;

@Configuration
@EnableBatchProcessing
public class ErrorDemo1 {

    @Autowired
    private JobBuilderFactory jobBuilders;
    @Autowired
    private StepBuilderFactory stepBuilder;
    @Bean
    public Job errorDemo1Job(){
        return jobBuilders.get("errorDemo1Job")
                .start(errorDemo1Step1())
                .next(errorDemo1Step2())
                .build();
    }

    @Bean
    public Step errorDemo1Step1() {
        return stepBuilder.get("errorDemo1Step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
                        if (stepExecutionContext.containsKey("info")) {
                            System.out.println("the second run will successful");
                            return RepeatStatus.FINISHED;
                        }else {
                            //模拟异常
                            System.out.println("the first run will fail");
                            Map<String, Object> stepExecutionContext1 = chunkContext.getStepContext().getStepExecutionContext();
                            chunkContext.getStepContext().getStepExecution().getExecutionContext().put("info","error");
                            throw new RuntimeException("error.........");
                        }
                    }
                }).build();
    }
    @Bean
    public Step errorDemo1Step2() {
        return stepBuilder.get("errorDemo1Step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        Map<String, Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
                        if (stepExecutionContext.containsKey("info")) {
                            System.out.println("the second run will successful");
                            return RepeatStatus.FINISHED;
                        }else {
                            //模拟异常
                            System.out.println("the first run will fail");
                            //第一次getStepExecutionContext获取不到，需要先获取执行器，然后//再获取执行器的上下文 chunkContext.getStepContext().getStepExecutionContext().put("info","error");
                            throw new RuntimeException("error.........");
                        }
                    }
                }).build();
    }

}