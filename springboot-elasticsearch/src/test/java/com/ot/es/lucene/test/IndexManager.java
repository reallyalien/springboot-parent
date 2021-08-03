package com.ot.es.lucene.test;

import com.ot.es.lucene.dao.SkuDao;
import com.ot.es.lucene.dao.impl.SkuDaoImpl;
import com.ot.es.lucene.pojo.Sku;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 索引管理
 */
public class IndexManager {

    private static final String INDEX_PATH = "D:\\software\\luceneindex";


    /**
     * 创建索引
     *
     * @throws IOException
     */
    @Test
    public void createIndex() throws IOException {
        //1.收集数据
        SkuDao dao = new SkuDaoImpl();
        List<Sku> skuList = dao.findAll();
        //2.创建document对象
        List<Document> documentList = new ArrayList<>();
        for (Sku sku : skuList) {
            Document document = new Document();
            //3.在document对象当中添加field域
            // 商品id 不分词，不索引，存储
            document.add(new StringField("id", sku.getId().toString(), Field.Store.YES));
            // 商品名称 分词，索引，存储
            document.add(new TextField("name", sku.getName(), Field.Store.YES));
            // 商品价格 分词，索引，不存储
            document.add(new FloatPoint("price", sku.getPrice()));
            // 品牌名称 不分词，索引，存储
            document.add(new StringField("brandName", sku.getBrandName(), Field.Store.YES));
            // 分类名称 不分词，索引，存储
            document.add(new StringField("categoryName", sku.getCategoryName(), Field.Store.YES));
            // 图片地址 不分词，不索引，存储
            document.add(new StoredField("image", sku.getImage()));
            documentList.add(document);
        }
        //4.创建分词器,分析文档，进行分词
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //5.创建directory对象，声明索引库的位置
        Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        //6.创建indexWriterConfig对象，写入索引需要的配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //7.创建indexWriter写入对象
        IndexWriter writer = new IndexWriter(directory, config);
        //8.写入到索引库
        for (Document document : documentList) {
            writer.addDocument(document);
        }
        writer.close();
    }

    @Test
    public void search() throws ParseException, IOException {
        //1.创建搜索对象
        StandardAnalyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("name", analyzer);
        Query query = queryParser.parse("name:手机 AND 华为");
        //2.声明索引库的位置
        FSDirectory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        //3.创建索引读取对象
        IndexReader reader = DirectoryReader.open(directory);
        //4.创建索引搜索对象
        IndexSearcher searcher = new IndexSearcher(reader);
        //5.使用索引搜索对象，执行搜索，返回结果集
        TopDocs docs = searcher.search(query, 10);
        System.out.println("查询到的总数是：" + docs.totalHits);
        //6.获取查询结果集
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //7.解析结果集
        for (ScoreDoc doc : scoreDocs) {
            int docID = doc.doc;
            Document document = searcher.doc(docID);
            System.out.println("=============================");
            System.out.println("docID:" + docID);
            System.out.println("id:" + document.get("id"));
            System.out.println("name:" + document.get("name"));
            System.out.println("price:" + document.get("price"));
            System.out.println("brandName:" + document.get("brandName"));
            System.out.println("image:" + document.get("image"));
        }
    }

    /**
     * 更新索引
     */
    @Test
    public void update() throws IOException {
        //创建分词器
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //创建流对象
        FSDirectory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(directory, config);
        Document document = new Document();
        document.add(new TextField("id", "1234567", Field.Store.YES));
        document.add(new TextField("name", "lucene测试", Field.Store.YES));

        //执行更新，会把符合条件的document删除然后再新增
        writer.updateDocument(new Term("id", "1234567"), document);
        writer.close();
    }

    /**
     * 测试 whitespaceAnalyzer分词器
     */
    @Test
    public void testWhitespaceAnalyzer() throws IOException {
        //1.创建分词器
        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        //2.指定索引路径
        FSDirectory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        //3.声明配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //4.创建indexWriter
        IndexWriter writer = new IndexWriter(directory, config);
        //5.创建文档
        Document document = new Document();
        document.add(new TextField("name", "vivo X23 8GB+128GB 幻夜蓝", Field.Store.YES));
        writer.addDocument(document);
        writer.close();
    }
}
