package com.ot.activiti.config;

import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.omg.PortableServer.ServantActivator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Objects;

/**
 * 如果数据库不自动创建表，则连接url增加 nullCatalogMeansCurrent=true
 */
@Configuration
public class SpringActivitiConfig {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
        SpringProcessEngineConfiguration engineConfiguration = new SpringProcessEngineConfiguration();
        engineConfiguration.setDataSource(dataSource);
        engineConfiguration.setTransactionManager(platformTransactionManager);
        //不自动创建表，需要表存在 "false";
        //先删除表，再创建表  "create-drop";
        //如果表不存在，先创建表 "true";
        engineConfiguration.setDatabaseSchemaUpdate("true");
        Resource[] resources = null;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources("classpath*:processes/*.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        engineConfiguration.setDeploymentResources(resources);
        engineConfiguration.setDeploymentMode("never-fail");
        return engineConfiguration;
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(springProcessEngineConfiguration());
        return factoryBean;
    }

    @Bean
    public RepositoryService repositoryService() throws Exception {
        return Objects.requireNonNull(processEngine().getObject()).getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() throws Exception {
        return Objects.requireNonNull(processEngine().getObject()).getRuntimeService();
    }

    @Bean
    public TaskService taskService() throws Exception {
        return Objects.requireNonNull(processEngine().getObject()).getTaskService();
    }

    @Bean
    public HistoryService historyService() throws Exception {
        return Objects.requireNonNull(processEngine().getObject()).getHistoryService();
    }

}
