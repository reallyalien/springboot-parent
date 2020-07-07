package com.ot.springbatch.process;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SkipListener {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private MySkipListener mySkipListener;

    @Bean
    public Job skipListenerJob(){
        return jobBuilderFactory.get("skipListener")
                .start(skipListenerStep())
                .build();
    }

    @Bean
    public Step skipListenerStep(){
        return stepBuilderFactory.get("skipListenerStep")
                .<String,String>chunk(10)
                .reader(skipListenerItemReader())
                .processor(skipListenerItemProcess())
                .writer(skipListenerItemWriter())
                .faultTolerant()
                .skip(CustomRetryException.class)
                .skipLimit(5)
                .listener(mySkipListener)
                .build();
    }
    @Bean
    public ItemReader<String> skipListenerItemReader(){
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(String.valueOf(i));
        }
        ListItemReader<String> reader = new ListItemReader<String>(list);
        return reader;
    }
    @Bean
    public ItemWriter<String> skipListenerItemWriter(){
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                list.forEach(System.out::println);
            }
        };
    }

    @Bean
    public ItemProcessor<String,String> skipListenerItemProcess(){
        return new ItemProcessor<String, String>() {

            private int attemptCount=0;
            @Override
            public String process(String s) throws Exception {
                System.out.println("process:"+s);
                if (s.equalsIgnoreCase("26")){
                    attemptCount++;
                    if (attemptCount >= 3){
                        System.out.println("retry "+attemptCount+"times success");
                        return String.valueOf(Integer.valueOf(s)* -1);
                    }else {
                        System.out.println("process the"+attemptCount+"times fail");
                        throw new CustomRetryException("process fail attemp"+attemptCount);
                    }
                }else {
                    return String.valueOf(Integer.valueOf(s)* -1);
                }
            }
        };
    }


    class CustomRetryException extends Exception{

        public CustomRetryException() {
        }

        public CustomRetryException(String message) {
            super(message);
        }
    }
    @Component
    class MySkipListener implements org.springframework.batch.core.SkipListener<String,String>{

        @Override
        public void onSkipInRead(Throwable throwable) {

        }

        @Override
        public void onSkipInWrite(String s, Throwable throwable) {

        }

        @Override
        public void onSkipInProcess(String s, Throwable throwable) {
            System.out.println(s+" occur exception "+throwable);
            //在跳过错误数据之后，然后执行成功这一批数据之后(reader,process,write)然后打印这句话，记录下来发生的异常
        }
    }
}
