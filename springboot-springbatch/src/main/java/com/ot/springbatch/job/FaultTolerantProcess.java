package com.ot.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.item.FaultTolerantChunkProcessor;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class FaultTolerantProcess {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job faultTolerantProcessJob() {
        return jobBuilderFactory.get("faultTolerantProcessJob").start(faultTolerantProcessStep()).build();
    }

    @Bean
    public Step faultTolerantProcessStep() {
        return stepBuilderFactory.get("faultTolerantProcessStep").chunk(5)
                .reader(this::reader)
                .build();
    }

    public ItemReader reader() {
        List<Integer> list = new ArrayList<>();
        ListItemReader<Integer> reader = new ListItemReader<Integer>(list);
        return reader;
    }

    public ItemWriter writer() {
        //ChunkProcessorChunkHandler 包裹下面的对象
        //FaultTolerantChunkProcessor相当于一个组合类，内部需要process和writer,
        return new ItemWriter() {
            @Override
            public void write(List items) throws Exception {

            }
        };
    }
}
