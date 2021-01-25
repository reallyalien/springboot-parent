package com.ot.springbatch.job;

import com.ot.springbatch.pojo.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class FileReader {

    @Autowired
    private JobBuilderFactory jobBuilders;
    @Autowired
    private StepBuilderFactory stepBuilders;
    @Bean
    public Job fileJob(){
        return jobBuilders.get("fileJob")
                .start(fileStep())
                .build();

    }

    @Bean
    public Step fileStep() {
        return stepBuilders.get("fileStep")
                .<Person,Person>chunk(10)
                .reader(demoFileReader())
                .writer(demoFileWriter())
                .build();
    }

    @Bean
    public ItemReader<Person> demoFileReader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("csv/persons.csv"));
        reader.setr
        //跳过第一行，第一行是表头
        reader.setLinesToSkip(1);
        //数据映射，解析   分界线/分词器 默认,分隔
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        //解析哪些字段
        tokenizer.setNames(new String[]{"id","name","age","nation","address"});
        //把解析出来的记录映射为一个对象,一个个字段对应
        DefaultLineMapper<Person> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<Person>() {
            @Override
            public Person mapFieldSet(FieldSet fieldSet) throws BindException {
                Person person = new Person();
                //这里可以根据参数顺序读取，也可以根据属性名字获取
                person.setId(fieldSet.readInt(0));
                person.setName(fieldSet.readString(1));
                person.setAge(fieldSet.readInt(2));
                person.setNation(fieldSet.readString(3));
                person.setAddress(fieldSet.readString("address"));
                return person;
            }
        });
        mapper.afterPropertiesSet();
        //设置行映射
        reader.setLineMapper(mapper);
        return reader;
    }

    @Bean
    public ItemWriter<Person> demoFileWriter() {
        return new ItemWriter<Person>() {
            @Override
            public void write(List<? extends Person> list) throws Exception {
                list.forEach(System.out::println);
            }
        };
    }
}
