package com.ot.springbatch.process;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
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
                list.forEach(item->{
//                    if (item .equals("30")){
//                        try {
//                            throw new CustomRetryException("aaa");
//                        } catch (CustomRetryException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    System.out.println(item);
                });
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

        /**
         * skip的实现是基于事务的支持的，在step当中若遇到一个可被跳过的exception,则当前的transaction会回滚.
         * spring batch会把reader读取到的item缓存下来，因此，当某一天记录出错导致skip的时候，则这条记录就会从cache当中被删除掉。
         *
         * 紧接着，spring batch会重新开启一个事务，并且这个事务处理的数据是去除掉skip记录的cache数据。如果配置了SkipListener，
         * 则在提交一个chunk之前，SkipListener的onSkipInProcess方法会被调用。
         * @param s
         * @param throwable
         */
        @Override
        public void onSkipInProcess(String s, Throwable throwable) {
            System.out.println(s+" occur exception "+throwable);
            //在提交一个chunk之前会被调用
            //在跳过错误数据之后，然后执行成功这一批数据之后(reader,process,write)然后打印这句话，记录刚刚跳过发生的异常
        }
    }
}
