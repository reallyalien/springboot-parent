package com.ot.es.lucene.pojo;

import lombok.Data;

public class Sku {

    //商品主键
    private String id;
    //商品名称
    private String name;
    //商品价格
    private Integer price;
    //商品库存
    private Integer num;
    //商品图片
    private String image;
    //分类名称
    private String categoryName;
    //品牌名称
    private String brandName;
    //规格
    private String spec;
    //销量
    private Integer saleNum;

    public Sku() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", image='" + image + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", spec='" + spec + '\'' +
                ", saleNum=" + saleNum +
                '}';
    }
}
