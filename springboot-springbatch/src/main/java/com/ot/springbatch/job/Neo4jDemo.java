package com.ot.springbatch.job;

import com.ot.springbatch.pojo.User;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.Neo4jItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class Neo4jDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ListenerDemo.MyJobListener myJobListener;
    @Autowired
    private ListenerDemo.MyStepListener myStepListener;
    @Autowired
    private ListenerDemo.MyChunkListener myChunkListener;

    //======================================================================
    @Bean
    public Job neo4jJob() {
        return jobBuilderFactory.get("neo4jJob")
                .start(neo4jStep())
                .build();
    }

    @Bean
    public Step neo4jStep() {
        return stepBuilderFactory.get("neo4jStep")
                .<Record, Record>chunk(300)
                .faultTolerant()
                .reader(neo4jReader())
                .writer(neo4jWriter())
                .build();
    }

    public ItemReader<Record> neo4jReader() {
//        Neo4jItemReader<User> reader = new Neo4jItemReader<>();
//        reader.setSessionFactory(sessionFactory);
//        reader.setTargetType(User.class);
//        reader.setStartStatement(" p=node(*) ");
//        reader.setMatchStatement(" (p:User) ");
//        reader.setReturnStatement(" properties(p) ");
//        reader.setParameterValues(new HashMap<>());
//        reader.setName(ClassUtils.getShortName(Neo4jDemo.class));
        MyNeo4jItemReader reader = new MyNeo4jItemReader();
        reader.setDriver(driver());
        reader.setName(ClassUtils.getShortName(Neo4jDemo.class));
        reader.setStartStatement(" p=node(*) ");
        reader.setMatchStatement(" (p:kkk) ");
        reader.setReturnStatement(" id(p),properties(p) ");
        reader.setOrderByStatement("id(p) asc");
        reader.setWhereStatement(null);
        reader.setPageSize(589);
        return reader;
    }

    public ItemProcessor<Record, Record> neo4jProcess() {
        return new ItemProcessor<Record, Record>() {
            @Override
            public Record process(Record item) throws Exception {
                item.setNullFlag(true);
                return item;
            }
        };
    }

    private int count;

    //FOREACH (props IN [{ a:1,b:3,name:'cat1' }, { a:4,b:6,name:'cat2' }]|
    //         CREATE (p:Cat{ a:props.a,b:props.b,name:props.name }))
    public ItemWriter<Record> neo4jWriter() {
        return new ItemWriter<Record>() {

            @Override
            public void write(List<? extends Record> items) throws Exception {
                for (Record item : items) {
                    Map<String, Object> columnAndValues =
                            item.getColumnAndValues();
                    Object id = columnAndValues.get("id");
                    System.out.println(id);
                }
            }
        };
    }

    private Driver driver() {
        return GraphDatabase.driver("bolt://127.0.0.1:7687", AuthTokens.basic("neo4j", "root"));
    }
}
