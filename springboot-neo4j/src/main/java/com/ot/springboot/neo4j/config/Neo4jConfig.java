//package com.ot.springboot.neo4j.config;
//
//import org.neo4j.driver.AuthTokens;
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.GraphDatabase;
//import org.neo4j.ogm.config.ClasspathConfigurationSource;
//import org.neo4j.ogm.config.ConfigurationSource;
//import org.neo4j.ogm.session.SessionFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
//import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
//
//@Configuration
//@EnableNeo4jRepositories(basePackages ={ "com.ot.springboot.neo4j.dao","com.ot.springboot.neo4j.demo1.dao"})
//public class Neo4jConfig {
//
//    @Bean
//    public org.neo4j.ogm.config.Configuration configuration() {
//        ConfigurationSource properties = new ClasspathConfigurationSource("application.properties");
//        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder(properties)
//                .build();
//
//        return configuration;
//    }
//
//    @Bean(name = "sessionFactory")
//    public SessionFactory getSessionFactory() {
//        /**
//         * 如果不指定节点映射的java bean路径，保存时会报如下警告，导致无法将节点插入Neo4j中 ... is not an instance of a
//         * persistable class
//         */
//        SessionFactory sessionFactory = new SessionFactory(configuration(), "com.ot.springboot.neo4j.domain");
//        return sessionFactory;
//    }
//
//    /**
//     * 解决MyBatis 事务与Neo4j 事务冲突
//     *
//     * @return
//     */
//
//    @Bean(name = "neo4jTransaction")
//    public Neo4jTransactionManager getDefaultTransactionManager(
//            @Qualifier("sessionFactory") SessionFactory sessionFactory) {
//        return new Neo4jTransactionManager(sessionFactory);
//    }
//
//    @Value("${URI}")
//    private String url;
//    @Value("${username}")
//    private String username;
//    @Value("${password}")
//    private String password;
//
//    @Bean
//    public Driver neo4jDriver(){
//        return GraphDatabase.driver(url, AuthTokens.basic(username,password));
//    }
//}
