package com.ot.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "product", shards = 3, replicas = 1)
public class Product implements Serializable {

    //唯一标识，相当于es当中的_id
    @Id
    private Long id;

    /**
     * type:字段数据类型
     * analyze:分词器类型
     * index:是否被索引，默认为true
     * Keyword：短语，不进行分词
     */
    //商品名称
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

    //分类名称
    @Field(type = FieldType.Keyword)
    private String category;

    //商品价格
    @Field(type = FieldType.Double)
    private Double price;

    //图片地址
    @Field(type = FieldType.Keyword, index = false)
    private String images;
}
