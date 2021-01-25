package com.ot.springbatch.job;

import com.ot.springbatch.pojo.People;
import com.zaxxer.hikari.HikariDataSource;
import org.neo4j.driver.Session;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class HiveJobDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    public ListenerDemo.MyChunkListener myChunkListener;

    private static Session session;

    @Bean
    public Job hiveDbJob() {
        return jobBuilderFactory.get("hiveDbJob")
                .start(hiveDbStep())
                .build();
    }

    @Bean
    public Step hiveDbStep() {
        return stepBuilderFactory.get("hiveDbStep")
                .<People, People>chunk(10000)
                .faultTolerant()
//                .listener(myChunkListener)
                .reader(hiveDbReader())
                .writer(hiveDbWriter())
                .build();
    }

//    @Bean
//    public ItemReader<People> hiveDbReader() {
//        JdbcPagingItemReader<People> reader = new JdbcPagingItemReader<>();
//        reader.setDataSource(dataSource());
//        //指定行映射
//        reader.setRowMapper(new RowMapper<People>() {
//            @Override
//            public People mapRow(ResultSet resultSet, int i) throws SQLException {
//                People people = new People();
//                people.setId(resultSet.getInt(1));
//                people.setName(resultSet.getString(2));
//                people.setHobby(resultSet.getString(3));
//                people.setAddress(resultSet.getString(4));
//                people.setDt(resultSet.getString(5));
//                return people;
//            }
//        });
//        //指定sql语句
//        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
//        provider.setFromClause("from people");//查询哪个表
//        provider.setSelectClause("id,name,hobby,address,dt");//查询哪些字段
//        provider.setWhereClause(" id<=20 ");
//        Map<String, Order> sort = new HashMap<>();
//        sort.put("id", Order.ASCENDING);
//        provider.setSortKeys(sort);//按哪个字段排序
//        reader.setQueryProvider(provider);
//        reader.setPageSize(10);
//        return reader;
//    }

    @Bean
    public ItemReader<People> hiveDbReader() {
//        JdbcPagingItemReader<People> reader = new JdbcPagingItemReader<>();
        JdbcCursorItemReader<People> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource());
        //指定行映射
        reader.setRowMapper(new RowMapper<People>() {
            @Override
            public People mapRow(ResultSet resultSet, int i) throws SQLException {
                People people = new People();
                people.setId(resultSet.getInt(1));
                people.setName(resultSet.getString(2));
                people.setHobby(resultSet.getString(3));
                people.setAddress(resultSet.getString(4));
                people.setDt(resultSet.getString(5));
                return people;
            }
        });
        //指定sql语句
        reader.setSql(" select * from people ");
        reader.setFetchSize(10000);
        return reader;
    }


    public ItemWriter<People> hiveDbWriter() {
        return new ItemWriter<People>() {
            @Override
            public void write(List<? extends People> list) throws Exception {
//                System.out.println("写线程：" + Thread.currentThread().getName());
//                for (People people : list) {
//                    System.out.println(people);
//                }
            }
        };
    }


    private String url="jdbc:hive2://192.168.100.83:10000";

    private String username="root";

    private String password="root";

    private String driver="org.apache.hive.jdbc.HiveDriver";

    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        dataSource.setJdbcUrl(url);
        return dataSource;
    }

}
