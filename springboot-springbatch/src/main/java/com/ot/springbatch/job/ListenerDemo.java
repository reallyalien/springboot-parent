package com.ot.springbatch.job;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ListenerDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private MyJobListener myJobListener;
    @Autowired
    private MyStepListener myStepListener;
    @Autowired
    private MyChunkListener myChunkListener;
    //===============================================================================================

    @Bean
    public Job listenerJob(){
        return jobBuilderFactory.get("listenerJob")
                .listener(myJobListener)
                .start(listenerStep1())
                .next(listenerStep2())
                .build();
    }
    @Bean
    public Step listenerStep1(){
        return stepBuilderFactory.get("listenerStep1")
                .<String,String>chunk(2)
                .faultTolerant()//容错
                .listener(myChunkListener)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Step listenerStep2(){
        return stepBuilderFactory.get("listenerStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(myStepListener)
                .build();
    }

    @Bean
    public ItemReader<String> reader(){
        return new ListItemReader<>(Arrays.asList("Java1","Java2","Java3"));
    }
    @Bean
    public ItemWriter<String> writer(){
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach(System.out::println);
            }
        };
    }






    @Component
    class MyJobListener implements JobExecutionListener{

        @Override
        public void beforeJob(JobExecution jobExecution) {
            System.out.println(jobExecution.getJobInstance().getJobName()+"job执行前");
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            System.out.println(jobExecution.getJobInstance().getJobName()+"job执行后");
        }
    }

    @Component
    class MyStepListener implements StepExecutionListener{

        @Override
        public void beforeStep(StepExecution stepExecution) {
            System.out.println(stepExecution.getStepName()+"step执行前");
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            System.out.println(stepExecution.getStepName()+"step执行后");
            return ExitStatus.COMPLETED;
        }
    }

    @Component
    class MyChunkListener implements ChunkListener{

        @Override
        public void beforeChunk(ChunkContext chunkContext) {
            StepContext stepContext = chunkContext.getStepContext();
            System.out.println(stepContext.getStepName()+"chunk执行前");
        }

        @Override
        public void afterChunk(ChunkContext chunkContext) {
            StepContext stepContext = chunkContext.getStepContext();
            System.out.println(stepContext.getStepName()+"chunk执行后");
        }

        @Override
        public void afterChunkError(ChunkContext chunkContext) {

        }
    }
}
