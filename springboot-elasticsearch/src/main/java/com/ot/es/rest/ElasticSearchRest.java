package com.ot.es.rest;

import com.ot.es.dao.ProductDao;
import com.ot.es.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/elasticSearch")
public class ElasticSearchRest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @GetMapping("/save")
    public void save() {
        Product product = Product.builder()
                .id(1L)
                .title("华为手机")
                .category("手机")
                .price(2999.00D)
                .images("http://1.jpg")
                .build();
        productDao.save(product);
    }

    @GetMapping("/update")
    public void update() {
        Product product = Product.builder()
                .id(1L)
                .title("华为手机")
                .category("手机")
                .price(3999.00D)
                .build();
        productDao.save(product);
    }

    @GetMapping("/findById")
    public Product findById() {
        Optional<Product> optional = productDao.findById(1L);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @GetMapping("/findAll")
    public void findAll() {
        Iterable<Product> all = productDao.findAll();
        all.forEach(System.out::println);
    }

    @GetMapping("/delete")
    public void delete() {
        productDao.deleteById(1L);
    }


    @GetMapping("/batchSave")
    public void batchSave() {
        List<Product> productList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            Product product = new Product();
            product.setId((long) i);
            product.setTitle("[" + i + "]小米手机");
            product.setCategory("手机");
            product.setPrice(1999.0 + i);
            product.setImages("http://www.atguigu/xm.jpg");
            productList.add(product);
        }
        productDao.saveAll(productList);
    }
    //======================================查询=====================================


}
