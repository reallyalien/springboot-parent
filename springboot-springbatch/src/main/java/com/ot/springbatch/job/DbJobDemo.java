package com.ot.springbatch.job;


import com.ot.springbatch.pojo.User;
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
public class DbJobDemo  {

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
    @Bean
    public Job dbJob(){
        return jobBuilderFactory.get("dbJob")
                .start(dbStep())
                .build();
    }

    @Bean
    public Step dbStep(){
        return stepBuilderFactory.get("dbStep")
                .<User,User>chunk(10)
                .faultTolerant()
                .listener(myChunkListener)
                .reader(dbReader())
                .writer(dbWriter())
                .build();
    }
    @Bean
    public ItemReader<User> dbReader(){
        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(datasource);
        reader.setFetchSize(5);
//        System.out.println("11111111111");
        //指定行映射
        reader.setRowMapper(new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user=new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setPassword(resultSet.getString(3));
                return user;
            }
        });
        //指定sql语句
        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setFromClause("from user");//查询哪个表
        provider.setSelectClause("id,name,password");//查询哪些字段
        Map<String, Order> sort=new HashMap<>();
        sort.put("id",Order.DESCENDING);
        provider.setSortKeys(sort);//按哪个字段排序

        reader.setQueryProvider(provider);
//        reader.afterPropertiesSet();
        return reader;
    }

    @Bean
    public ItemWriter<User> dbWriter(){
       return new ItemWriter<User>() {
           @Override
           public void write(List<? extends User> list) throws Exception {
               list.forEach(n-> System.out.println(n));
           }
       };
    }
}
