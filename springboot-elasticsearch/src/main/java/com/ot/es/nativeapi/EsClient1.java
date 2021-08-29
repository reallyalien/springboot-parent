package com.ot.es.nativeapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ot.es.entity.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EsClient1 {

    private RestHighLevelClient client;

    /**
     * 测试连接
     *
     * @throws IOException
     */
    @Before
    public void connection() throws IOException {
        HttpHost httpHost = new HttpHost("localhost", 1001, "http");
        RestClientBuilder builder = RestClient.builder(httpHost);
        client = new RestHighLevelClient(builder);
//        client.close();
    }

    /**
     * 创建索引，当硬盘容量超出85%之后，副本无法分配，注意
     */
    @Test
    public void addIndex() throws IOException {
        //创建索引对象--请求对象
        CreateIndexRequest request = new CreateIndexRequest("user");
        HashMap<String, Integer> map = new HashMap<>();
        map.put("number_of_shards", 3);
        map.put("number_of_replicas", 1);
        request.settings(map);
        //发送请求，获取相应
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        //响应结果
        System.out.println(response);
    }

    /**
     * 查看索引
     */
    @Test
    public void getIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("user");
        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
        System.out.println("aliases:" + response.getAliases());
        System.out.println("mapping:" + response.getMappings());
        System.out.println("setting:" + response.getSettings());
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("user");
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        boolean ack = response.isAcknowledged();
        System.out.println("删除是否成功：" + ack);
    }

    @Test
    public void addDoc() throws IOException {
        //新增文档请求对象
        IndexRequest request = new IndexRequest();
        //设置索引和唯一标识
        request.index("user").id("1001");
        //创建数据对象
        User user = new User();
        user.setAge(10);
        user.setName("张三");
        user.setSex("男");
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(user);
        request.source(productJson, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        ////3.打印结果信息
        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
    }

    /**
     * 修改文档
     */
    @Test
    public void updateDoc() throws IOException {
        UpdateRequest request = new UpdateRequest();
        //配置修改参数
        request.index("user").id("1001");
        //设置请求体，修改数据
        request.doc(XContentType.JSON, "sex", "女");
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
    }

    /**
     * 查询文档
     */
    @Test
    public void findDoc() throws IOException {
        GetRequest request = new GetRequest();
        request.index("user").id("1001");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        ////3.打印结果信息
        System.out.println("_index:" + response.getIndex());
        System.out.println("_type:" + response.getType());
        System.out.println("_id:" + response.getId());
        System.out.println("source:" + response.getSourceAsString());
    }

    /**
     * 删除文档
     */
    @Test
    public void delDoc() throws IOException {
        DeleteRequest request = new DeleteRequest();
        request.index("user").id("1001");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 批量新增
     */
    @Test
    public void batchAdd() throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(new IndexRequest().index("user").id("1001").source(XContentType.JSON, "name", "zhangsan"));
        request.add(new IndexRequest().index("user").id("1002").source(XContentType.JSON, "name", "lisi"));
        request.add(new IndexRequest().index("user").id("1003").source(XContentType.JSON, "name", "wangwu"));
        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);
        //打印结果信息
        System.out.println("took:" + responses.getTook());
        System.out.println("items:" + responses.getItems());
    }
    //===========================================高级查询================================================

    /**
     * 查询所有索引数据
     */
    @Test
    public void queryAll() throws IOException {
        //创建搜索索引对象
        SearchRequest request = new SearchRequest();
        request.indices("user");
        //创建查询的请求体
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //查询所有数据
        builder.query(QueryBuilders.matchAllQuery());
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    }

    /**
     * term关键词查询，查询条件为关键字，不对关键字分词
     */
    @Test
    public void term() throws IOException {
        SearchRequest request = new SearchRequest();
//        request.indices("user");
        request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("name", "zhangsan")));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        print(response);

    }

    public void print(SearchResponse response) {
        // 查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
    }

    /**
     * 分页排序
     */
    @Test
    public void pageAndSort() throws IOException {
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
        builder.from(0);
        builder.size(2);
        builder.sort("age", SortOrder.DESC);
        request.source(builder);
        request.indices("shopping");
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        print(response);
    }

    /**
     * 查询过滤字段
     */
    @Test
    public void filter() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("shopping");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());

        //查询字段过滤
        String[] includes = {"name", "age"};
        String[] exclude = {};
        builder.fetchSource(includes, exclude);

        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        print(response);
    }

    @Test
    public void bool() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("shopping");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //必须包含
        boolQuery.must(QueryBuilders.matchQuery("age", 30));
        //一定不含
        boolQuery.mustNot(QueryBuilders.matchQuery("name", "zhangsan"));
        //可能包含
        boolQuery.should(QueryBuilders.matchQuery("sex", "男"));
        builder.query(boolQuery);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        print(response);
    }

    /**
     * 范围查询
     */
    @Test
    public void range() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("shopping");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");
        rangeQuery.gte("30");
        rangeQuery.lte("40");
        builder.query(rangeQuery);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        print(response);
    }

    /**
     * 模糊查询
     * fuzzy 模糊查询  最大模糊错误 必须在0-2之间
     * 搜索关键词长度为 2 不允许存在模糊 0
     * 搜索关键词长度为3-5 允许一次模糊 0 1
     * 搜索关键词长度大于5 允许最大2模糊
     */
    @Test
    public void fuzzy() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("shopping");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        FuzzyQueryBuilder fuzzyQuery = QueryBuilders.fuzzyQuery("name", "zhangsan");
        fuzzyQuery.fuzziness(Fuzziness.ONE);
        builder.query(fuzzyQuery);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        print(response);
    }

    /**
     * 高亮查询
     */
    @Test
    public void highLight() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("shopping");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        TermQueryBuilder termQuery = QueryBuilders.termQuery("name", "zhangsan");
        builder.query(termQuery);
        //构建高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        builder.highlighter(highlightBuilder);

        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //4.打印响应结果
        SearchHits hits = response.getHits();
        System.out.println("took::" + response.getTook());
        System.out.println("time_out::" + response.isTimedOut());
        System.out.println("total::" + hits.getTotalHits());
        System.out.println("max_score::" + hits.getMaxScore());
        System.out.println("hits::::>>");
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            //打印高亮结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            System.out.println(highlightFields);
        }
        System.out.println("<<::::");

    }

    /**
     * 其实准确来说，ES中的查询操作分为2种: 查询(query)和过滤(filter)。查询即是之前提到的query查询，它 (查询)默认会计算
     * 每个返回文档的得分，然后根据得分排序。而过滤(filter)只会筛选出符合的文档，并不计算 得分，且它可以缓存文档 。所以，
     * 单从性能考虑，过滤比查询更快。
     * 换句话说，过滤适合在大范围筛选数据，而查询则适合精确匹配数据。一般应用时， 应先使用过滤操作过滤数据， 然后使用查询匹配数据。
     */
    @Test
    public void aggregation1() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("shopping");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        MaxAggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
        builder.aggregation(aggregationBuilder);
        request.source(builder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        System.out.println(response.getHits());
    }

}
