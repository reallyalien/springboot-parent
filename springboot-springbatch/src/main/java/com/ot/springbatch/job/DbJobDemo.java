package com.ot.springbatch.job;


import com.ot.springbatch.pojo.Dept;
import com.ot.springbatch.pojo.User;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableBatchProcessing
public class DbJobDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource datasource;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    public ListenerDemo.MyChunkListener myChunkListener;

    private static Session session;

    @Bean
    public Job dbJob() {
        return jobBuilderFactory.get("dbJob")
                .start(dbStep())
                .build();
    }

    @Bean
    public Step dbStep() {
        return stepBuilderFactory.get("dbStep")
                .<Dept, Dept>chunk(10000)
                .faultTolerant()
//                .listener(myChunkListener)
                .reader(dbReader())
                .writer(dbWriter())
                .build();
    }

    @Bean
    public ItemReader<Dept> dbReader() {
        JdbcPagingItemReader<Dept> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(datasource);
//        System.out.println("11111111111");
        //指定行映射
        reader.setRowMapper(new RowMapper<Dept>() {
            @Override
            public Dept mapRow(ResultSet resultSet, int i) throws SQLException {
                Dept dept = new Dept();
                dept.setId(resultSet.getInt(1));
                dept.setDname(resultSet.getString(2));
                dept.setDb_source(resultSet.getString(3));
                return dept;
            }
        });
        //指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setFromClause("from dept");//查询哪个表
        provider.setSelectClause("id,dname,db_source");//查询哪些字段
        Map<String, Order> sort = new HashMap<>();
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);//按哪个字段排序
        reader.setQueryProvider(provider);
        reader.setPageSize(20000);
        return reader;
    }


    public ItemWriter<Dept> dbWriter() {
        return new ItemWriter<Dept>() {
            @Override
            public void write(List<? extends Dept> list) throws Exception {
                System.out.println("写线程："+Thread.currentThread().getName());
            }
        };
    }
    //FOREACH (props IN [{ a:1,b:3,name:'cat1' }, { a:4,b:6,name:'cat2' }]| CREATE (p:Cat{ a:props.a,b:props.b,name:props.name }))
    //写neo4j数据库
//    public ItemWriter<Dept> dbWriter() {
//        return new ItemWriter<Dept>() {
//            @Override
//            public void write(List<? extends Dept> list) throws Exception {
//                StringBuilder builder = new StringBuilder();
//                builder.append("FOREACH (props IN [");
//                for (Dept dept : list) {
//                    builder.append("{");
//                    builder.append("dname:'" + dept.getDname() + "',db_source:'" + dept.getDb_source() + "'");
//                    builder.append("},");
//                }
//                String s = builder.substring(0, builder.length() - 1);
//                builder = new StringBuilder(s);
//                builder.append("]| CREATE (p:" + Dept.class.getSimpleName().toLowerCase());
//                builder.append("{");
//                builder.append("dname:props.dname,db_source:props.db_source");
//                builder.append("}");
//                builder.append("))");
//                Map params = new HashMap();
//                Result run = session.run(builder.toString(), params);
//                List<Record> list1 = run.list();
//                for (Record record : list1) {
//                    System.out.println(record);
//                }
//            }
//        };
//    }

    static {
        Driver driver = getDriver("bolt://127.0.0.1:7687", "neo4j", "root");
        session = driver.session();
    }

    private static Driver getDriver(String url, String username, String password) {
        return GraphDatabase.driver(url, AuthTokens.basic(username, password));
    }
}
