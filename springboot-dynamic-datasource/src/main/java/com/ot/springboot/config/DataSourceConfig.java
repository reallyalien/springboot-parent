package com.ot.springboot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ot.springboot.dynamic.DynamicDataSource;
import com.ot.springboot.dynamic.DynamicDataSourceEnum;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {
    @Autowired
    private MasterProperties masterProperties;
    @Autowired
    private SlaveProperties slaveProperties;

    @Bean
    @Primary
    public DataSource master() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(masterProperties.getDriverClassName());
        druidDataSource.setUrl(masterProperties.getUrl());
        druidDataSource.setUsername(masterProperties.getUsername());
        druidDataSource.setPassword(masterProperties.getPassword());

        druidDataSource.setInitialSize(masterProperties.getInitialSize());
        druidDataSource.setMinIdle(masterProperties.getMinIdle());
        druidDataSource.setMaxActive(masterProperties.getMaxActive());
        druidDataSource.setMaxWait(masterProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(masterProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(masterProperties.getMinEvictableIdleTimeMillis())
        ;
        druidDataSource.setValidationQuery(masterProperties.getValidationQuery());
        return druidDataSource;
    }

    @Bean
    public DataSource slave() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(slaveProperties.getDriverClassName());
        druidDataSource.setUrl(slaveProperties.getUrl());
        druidDataSource.setUsername(slaveProperties.getUsername());
        druidDataSource.setPassword(slaveProperties.getPassword());
        return druidDataSource;
    }

    /**
     * 注意下面的方法注入的bean一定得是dynamicDataSource,否则动态切换数据源不成功
     * @return
     */
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        HashMap<Object, Object> targetSourceMap = new HashMap<>();
        targetSourceMap.put(DynamicDataSourceEnum.MASTER.getDataSourceName(), master());
        targetSourceMap.put(DynamicDataSourceEnum.SLAVE.getDataSourceName(), slave());
        dynamicDataSource.setTargetDataSources(targetSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(master());
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("classpath*:mapper/*.xml");
//        sqlSessionFactoryBean.setTypeAliasesPackage("com.ot.springboot.entities");
        sqlSessionFactoryBean.setMapperLocations(resources);
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(StdOutImpl.class);
        sqlSessionFactoryBean.setConfiguration(configuration);
        return sqlSessionFactoryBean;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(@Qualifier("dynamicDataSource") DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }
}
