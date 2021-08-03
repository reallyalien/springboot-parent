package com.ot.springboot.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//统一指定缓存的名称
@CacheConfig(cacheNames = "")
@RestController
@RequestMapping("/cache")
public class CacheController {

    private static final Map<String, String> map = new ConcurrentHashMap<>();

    @Autowired
    private CacheController cacheController; //是一个代理对象

    static {
        map.put("1", "A");
        map.put("2", "B");
        map.put("3", "C");
    }

    @GetMapping("/update/{key}/{value}")
    public String update(@PathVariable("key") String key, @PathVariable("value") String value) {
        map.put(key, value);
        return value;
    }

    //此注解会先查询有没有缓存，没有的查询出来放入缓存，如果有的话就直接查缓存
    @Cacheable(value = "query", key = "11")
    @GetMapping("/query/{key}")
    public String query(@PathVariable("key") String key) {
        return map.get(key);
    }


    //主要针对方法配置，能够根据方法的请求参数对结果进行缓存，与@Cacheable不同的是，它每次都会触发真实的方法调用，简单来说就是用户更新
    //缓存数据，但需要注意的是该注解的value和key的值需要与@Cacheable的key和value相同
    @CachePut(value = "query", key = "11")
    @GetMapping("/put/{key}")
    public String put(@PathVariable("key") String key) {
        return map.get(key);
    }

    //allEntries在方法调用之后清空缓存
    //beforeInvocation在方法执行之前清空缓存，如果方法执行抛出异常，则不会清空缓存
    @CacheEvict(value = "query", allEntries = true,key = "11")
    @GetMapping("/delete")
    public void delete() {

    }
}
