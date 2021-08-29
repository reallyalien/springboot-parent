package com.ot.es.dao;

import com.ot.es.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * 此处非响应式编程，响应式编程使用 ReactiveElasticsearchRepository
 * <p>
 * spring data 支持响应式编程的nosql
 * MongoDB：使用spring-boot-starter-data-mongodb-reactive依赖；
 * Redis：使用spring-boot-starter-data-redis-reactive依赖；
 * Cassandra：使用spring-boot-starter-data-cassandra-reactive依赖；
 * Couchbase：使用spring-boot-starter-data-couchbase-reactive依赖；
 * Elasticsearch：使用spring-boot-starter-data-elasticsearch依赖；
 * ————————————————
 */
@Repository
public interface ProductDao extends ElasticsearchRepository<Product, Long> {

}
