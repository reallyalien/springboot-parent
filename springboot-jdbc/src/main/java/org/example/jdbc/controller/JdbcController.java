package org.example.jdbc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Title: JdbcController
 * @Author wangtao
 * @Date 2023/10/9 10:34
 * @description:
 */
@RequestMapping("/jdbc")
@RestController
public class JdbcController {

    @GetMapping("/starRocks")
    public String starRocks() {
        final String url = "jdbc:mysql://192.168.2.122:9030";
        final String name = "com.mysql.jdbc.Driver";
        final String user = "root";
        final String password = "root";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            String sql = "SELECT  a.TASK_ID task_id, count(1) bj_num\n" +
                    "from iceberg_catalog_122.iceberg_test.audit_project_160w a,\n" +
                    "     iceberg_catalog_122.iceberg_test.audit_task b\n" +
                    "where a.TASK_ID = b.TASK_ID\n" +
                    "  and a.AREACODE = '140400'\n" +
                    "  and b.IS_ENABLE = 1\n" +
                    "  and b.IS_EDITAFTERIMPORT = 1\n" +
                    "  and (b.IS_HISTORY is null or b.IS_HISTORY = 0)\n" +
                    "group by a.TASK_ID;";
            PreparedStatement ps = conn.prepareStatement(sql);
            long s = System.currentTimeMillis();
            ResultSet rs = ps.executeQuery();
            long e = System.currentTimeMillis();
            String s1 = "耗时" + (e - s) + "毫秒";
            return s1;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @GetMapping("/trino")
    public String trino() {
        final String url = "jdbc:trino://192.168.2.123:8285/iceberg1";
        final String name = "com.mysql.jdbc.Driver";
        final String user = "root";
        final String password = "";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
//            String sql = "select * from iceberg1.iceberg_test.audit_project_160w ";
//            String sql = "SELECT count(a.RowGuid) from iceberg1.iceberg_test.audit_project_160w a WHERE\n" +
//                    "        a.status in (26,28,30,40,50,90,97,98,99,110)\n" +
//                    "        and\n" +
//                    "        a.Operatedate > '2003-01-01 00:00:01'  and a.Operatedate < '2033-01-01 00:00:01'\n" +
//                    "        and a.TASKTYPE = 2\n" +
//                    "        and a.FLOWSN !='null'";
            String sql = "SELECT a.TASK_ID task_id, count(1) bj_num\n" +
                    "from iceberg1.iceberg_test.audit_project_160w a,\n" +
                    "     iceberg1.iceberg_test.audit_task b\n" +
                    "where a.TASK_ID = b.TASK_ID\n" +
                    "  and a.AREACODE = '140400'\n" +
                    "  and b.IS_ENABLE = 1\n" +
                    "  and b.IS_EDITAFTERIMPORT = 1\n" +
                    "  and (b.IS_HISTORY is null or b.IS_HISTORY = 0)\n" +
                    "group by a.TASK_ID";
            PreparedStatement ps = conn.prepareStatement(sql);
            long s = System.currentTimeMillis();
            ResultSet rs = ps.executeQuery();
            long e = System.currentTimeMillis();
            String s1 = "耗时" + (e - s) + "毫秒";
            return s1;
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
