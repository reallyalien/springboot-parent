package com.ot.springbatch.job;

import com.ot.springbatch.pojo.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

@Configuration
@EnableBatchProcessing
public class ExceptionDemo {

    @Autowired
    private JobBuilderFactory jobBuilders;
    @Autowired
    private StepBuilderFactory stepBuilders;
    @Autowired
    private ExceptionItemReader exceptionItemReader;
    @Bean
    public Job exceptionJob(){
        return jobBuilders.get("exceptionJob")
                .start(exceptionStep())
                .build();
    }

    @Bean
    public Step exceptionStep() {
        return stepBuilders.get("exceptionStep")
                .<Person,Person>chunk(2)
                .reader(exceptionItemReader)
                .writer(list -> {
                    list.forEach(System.out::println);
                }).build();

    }
    @Component
    class ExceptionItemReader implements ItemStreamReader<Person> {
        FlatFileItemReader<Person> reader=new FlatFileItemReader<>();
        private Long currLine=0L;
        private boolean restart=false;
        private ExecutionContext executionContext;

        public ExceptionItemReader() {
            reader.setResource(new ClassPathResource("csv/persons.csv"));
//           reader.setLinesToSkip(1);
            //数据映射，解析
            DefaultLineMapper<Person> mapper = new DefaultLineMapper<>();
            DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();//界限分词器
            tokenizer.setNames(new String[]{"id","name","age","nation","address"});
            mapper.setLineTokenizer(tokenizer);
            mapper.setFieldSetMapper(new FieldSetMapper<Person>() {
                @Override
                public Person mapFieldSet(FieldSet fieldSet) throws BindException {
                    Person person = new Person();
                    person.setId(fieldSet.readInt("id"));
                    person.setName(fieldSet.readString("name"));
                    person.setAge(fieldSet.readInt("age"));
                    person.setNation(fieldSet.readString("nation"));
                    person.setAddress(fieldSet.readString("address"));
                    return person;
                }
            });
            //设置行映射
            reader.setLineMapper(mapper);
        }
        //读数据一行一行读取，达到chunk的时候write一次
        @Override
        public Person read() throws Exception {
            Person person=null;
            //每次读取一行，当前行++
            this.currLine++;
            if (restart){
                reader.setLinesToSkip(this.currLine.intValue()-1);
                restart=false;
                System.out.println("start read ;"+this.currLine);
            }
            //相当于自己重写了一个read方法，自己无法具体去实现，还是调用其他类的read方法，在read之前去open
            reader.open(this.executionContext);
            person=reader.read();
            if (person!=null && person.getName().equals("wrong")){
                throw new RuntimeException("something wrong person.id:"+person.getId());
            }
            return person;
        }
        //每个step执行之前执行
        @Override
        public void open(ExecutionContext executionContext) throws ItemStreamException {
            this.executionContext=executionContext;
            if (executionContext.containsKey("currLine")){
                //这里既然有当前行这个保留信息，说明之前执行出错，需要重新执行。因此设置restart=true
                this.currLine=executionContext.getLong("currLine");
                this.restart=true;
            }else {
                this.currLine=0L;
//                executionContext.put("currLine",this.currLine);
                System.out.println("start reading from line "+this.currLine+1);
            }
        }

        //每一个chunk执行成功之后执行,第一次也会执行,最后一次也会执行。
        @Override
        public void update(ExecutionContext executionContext) throws ItemStreamException {
//            executionContext.put("currLine",this.currLine);
            System.out.println("currLine :"+this.currLine);
        }
        //step执行完之后执行
        @Override
        public void close() throws ItemStreamException {

        }
    }

}