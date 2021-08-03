package com.springboot.jta.atomikos.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.ot.springboot.dynamic.DynamicDataSource;
import com.ot.springboot.dynamic.DynamicDataSourceEnum;
import com.springboot.jta.atomikos.dynamic.DynamicDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {
    @Autowired
    private MasterProperties masterProperties;
    @Autowired
    private SlaveProperties slaveProperties;

    @Bean
    @Primary
    public DruidXADataSource master() {
        DruidXADataSource dataSource = new DruidXADataSource();
        dataSource.setDriverClassName(masterProperties.getDriverClassName());
        dataSource.setUrl(masterProperties.getUrl());
        dataSource.setUsername(masterProperties.getUsername());
        dataSource.setPassword(masterProperties.getPassword());
        return dataSource;
    }

    @Bean
    public DruidXADataSource slave() {
        DruidXADataSource dataSource = new DruidXADataSource();
        dataSource.setDriverClassName(slaveProperties.getDriverClassName());
        dataSource.setUrl(slaveProperties.getUrl());
        dataSource.setUsername(slaveProperties.getUsername());
        dataSource.setPassword(slaveProperties.getPassword());
        return dataSource;
    }

    //创建atomikos管理的XA数据源
    @Bean
    public DataSource dataSource1(@Autowired DruidXADataSource master) {
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSource(master);
        dataSourceBean.setUniqueResourceName("db1");
        return dataSourceBean;
    }

    @Bean
    public DataSource dataSource2(@Autowired DruidXADataSource slave) {
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSource(slave);
        dataSourceBean.setUniqueResourceName("db2");
        return dataSourceBean;
    }

    @Autowired
    private DataSource dataSource1;
    @Autowired
    private DataSource dataSource2;

    /**
     * 注意下面的方法注入的bean一定得是dynamicDataSource,否则动态切换数据源不成功
     *
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> targetSourceMap = new HashMap<>();
        targetSourceMap.put("db1", dataSource1);
        targetSourceMap.put("db2", dataSource2);
        dynamicDataSource.setTargetDataSources(targetSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(master());
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        return createSqlSessionFactory(dataSource1);
    }


    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("classpath*:mapper/*.xml");
//        sqlSessionFactoryBean.setTypeAliasesPackage("com.ot.springboot.entities");
        sqlSessionFactoryBean.setMapperLocations(resources);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(StdOutImpl.class);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dynamicDataSource") DataSource dataSource) {
//        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        JtaTransactionManager tm=new JtaTransactionManager();
        tm.
        return dataSourceTransactionManager;
    }

}
