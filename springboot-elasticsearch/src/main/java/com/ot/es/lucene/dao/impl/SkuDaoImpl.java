package com.ot.es.lucene.dao.impl;

import com.ot.es.lucene.dao.SkuDao;
import com.ot.es.lucene.pojo.Sku;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SkuDaoImpl implements SkuDao {

    @Override
    public List<Sku> findAll() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Sku> list = new ArrayList<>(100_0000);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lucene", "root", "root");
            String sql = "SELECT * FROM tb_sku";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sku sku = new Sku();
                sku.setId(rs.getString("id"));
                sku.setName(rs.getString("name"));
                sku.setSpec(rs.getString("spec"));
                sku.setBrandName(rs.getString("brand_name"));
                sku.setCategoryName(rs.getString("category_name"));
                sku.setImage(rs.getString("image"));
                sku.setNum(rs.getInt("num"));
                sku.setPrice(rs.getInt("price"));
                sku.setSaleNum(rs.getInt("sale_num"));
                list.add(sku);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
