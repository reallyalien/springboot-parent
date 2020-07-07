package com.ot.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class FuziJob {

    @Autowired
    private JobBuilderFactory jobBuilders;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private JobLauncher jobLauncher;
    //=============================================================================================================
    @Bean
    public Job childJob1(){
        return jobBuilders.get("childJob1")
                .start(childJobStep1())
                .build();
    }
    @Bean
    public Step childJobStep1(){
        return stepBuilders
                .get("childJobStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("childJobStep1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    //==============================================================================================================


    @Bean
    public Step parentJobStep1(){
        return stepBuilders
                .get("parentJobStep1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("parentJobStep1");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Step parentJobStep2(){
        return stepBuilders
                .get("parentJobStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("parentJobStep2");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Job parentJob1(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return jobBuilders.get("parentJob1")
                .start(childJobToStep1(jobRepository,transactionManager))
                .next(parentJobStep1())
                .next(parentJobStep2())
                .build();
    }

    @Bean
    public Step childJobToStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("childer"))
                .job(childJob1())
                .launcher(jobLauncher)
                .transactionManager(transactionManager)
                .repository(jobRepository)
                .build();
    }

}
