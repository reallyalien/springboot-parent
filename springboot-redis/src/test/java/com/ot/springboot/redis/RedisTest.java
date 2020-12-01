package com.ot.springboot.redis;


import com.ot.springboot.redis.demo.vote.service.RedisArticleService;

import com.ot.springboot.redis.utils.JedisUtils;
import org.apache.catalina.util.URLEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = RedisMain.class)
public class RedisTest {

    @Autowired
    private RedisArticleService redisArticleService;

    /**
     * 测试用例：用户发布文章
     */
    @Test
    public void test1(){
        System.out.println(redisArticleService);
    }
    @Test
    public void apostArticle() {
        String userId = "001"; //用户ID 001
        String title = "字母";
        String content = "abcdefghijklmnopqrstuvwxyz";
        String link = "www.abc.com";
        //发布文章，返回文章ID
        String articleId = redisArticleService.postArticle(title, content, link, userId);

        System.out.println("刚发布了一篇文章，文章ID为: " + articleId);
        System.out.println("文章所有属性值内容如下:");
        Map<String,String> articleData = redisArticleService.hgetAll("article:" + articleId);
        for (Map.Entry<String,String> entry : articleData.entrySet()){
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }
    /**
     * 测试用例：用户对文章投票
     */
    @Test
    public void barticleVote(){
        String userId = "002";
        String articleId = "1";

        System.out.println("开始对文章"+"article:" + articleId+"进行投票啦~~~~~");
        //cang用户给James的文章投票
        redisArticleService.articleVote(userId, "article:" + articleId);//article:1
        //投票完后，查询当前文章的投票数votes
        String votes = redisArticleService.hget("article:" + articleId, "votes");

        System.out.println("article:" + articleId+"这篇文章的投票数从redis查出来结果为: " + votes);
    }
    /**
     * 测试用例：获取文章列表并打印出来
     */
    @Test
    public void cgetArticles(){
        int page = 1;
        String key = "score:info";
        System.out.println("查询当前的文章列表集合为：");
        List<Map<String,String>> articles = redisArticleService.getArticles(page,key);
        for (Map<String,String> article : articles){
            System.out.println("  id: " + article.get("id"));
            for (Map.Entry<String,String> entry : article.entrySet()){
                if (entry.getKey().equals("id")){
                    continue;
                }
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    @Test
    public void jedisTest() throws IOException {
        JedisUtils jedisUtils = new JedisUtils();
        String path="d:/16.txt";
        FileInputStream fis = new FileInputStream(path);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        String encodeToString = Base64Utils.encodeToString(bytes);
        jedisUtils.set("key", encodeToString);
        System.out.println(jedisUtils.get("key"));

        String value = jedisUtils.get("key");
        byte[] bytes1 = Base64Utils.decodeFromString(value);
    }
}
