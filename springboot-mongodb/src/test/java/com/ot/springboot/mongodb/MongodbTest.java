package com.ot.springboot.mongodb;


import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ot.springboot.mongodb.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = MongodbMain.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class MongodbTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void objectId() {
        CollectionOptions options = new CollectionOptions(0b1010L, 1024L, true);
        mongoTemplate.createCollection("a", options);//在插入文档时，先检查size：容量，然后查看max的数值大小
    }

    @Test
    public void insert() {
        mongoTemplate.insert(new User("coly", "123456"));
        System.out.println("插入成功");
        List<User> programmers = new ArrayList<User>();
        // 批量插入
        programmers.add(new User("pack", "1xxx56"));
        programmers.add(new User("peter", "xxx56"));
        mongoTemplate.insert(programmers, User.class);
    }

    @Test
    public void select() {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is("coly"));
        Query query = new Query(criteria);
        User one = mongoTemplate.findOne(query, User.class);
        System.out.println(one);
        System.out.println("=====================================");
        System.out.println("查询所有");
        System.out.println(JSON.toJSONString(mongoTemplate.findAll(User.class)));
    }

    @Test
    public void update() {
        //updateMulti更新多个
        //updateUpsert,//true  如果存在则更新，如果不存在则插入
        UpdateResult updateResult = mongoTemplate.updateMulti(Query.query(Criteria.where("username").is("pack")), Update.update("password", "abcdef"), User.class);
        System.out.println(updateResult.getUpsertedId());
    }

    @Test
    public void del() {
        DeleteResult deleteResult = mongoTemplate.remove(Query.query(Criteria.where("username").is("coly")), User.class);
        System.out.println("影响记录数：" + deleteResult.getDeletedCount());
        System.out.println("是否成功：" + deleteResult.wasAcknowledged());
    }

    @Test
    public void aggregation() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sort(Sort.by(Sort.Order.desc("_id"))),
                Aggregation.group(new String[]{"username"})
                        .first("username").as("username")
                        .first("password").as("password")
        );
        AggregationResults<User> user = mongoTemplate.aggregate(aggregation, "user", User.class);
        List<User> results = user.getMappedResults();
        System.out.println(results);

    }
}
