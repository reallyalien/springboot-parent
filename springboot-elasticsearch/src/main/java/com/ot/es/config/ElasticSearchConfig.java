//package com.ot.es.config;
//
//import lombok.Data;
//import lombok.Setter;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//
//@ConfigurationProperties(prefix = "elasticsearch")
//@Configuration
//@Setter
//public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
//
//    //ElasticsearchRestTemplate
//
//    private String host;
//    private Integer port;
//
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        RestClientBuilder builder = RestClient.builder(new HttpHost(host, port));
//        return new RestHighLevelClient(builder);
//    }
//}
