package org.example.excel.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class DatasourceUtil {

    @Autowired
    private DataSource dataSource;

    public void createTableAndInsert(String tableName, List<String> columns, int[] len, List<List<Object>> result) {
        Connection connection = null;
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        try {
            connection = dataSource.getConnection();
            StringBuilder sb = new StringBuilder("");
            sb.append(" CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
            String varchar = "varchar(255)";
            String text = "text";
            for (int i = 0; i < columns.size(); i++) {
                int columnLen = len[i];
                sb.append(columns.get(i)).append(" ").append(columnLen > 255 ? text : varchar).append(",");
            }
            String createSql = sb.substring(0, sb.length() - 1) + " ) ";
            createPreparedStatement = connection.prepareStatement(createSql);
            createPreparedStatement.execute();
            log.info("创建表：[{}]成功", tableName);
            insertPreparedStatement = getInsertPreparedStatement(connection, tableName, columns);
            write(connection, insertPreparedStatement, result);
            log.info("写入表[{}]数据:[{}]", tableName, result.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PreparedStatement getInsertPreparedStatement(Connection connection, String table,
                                                               List<String> columns) throws SQLException {
        String insertSql = getInsertSql(table, columns);
        PreparedStatement ps = connection.prepareStatement(insertSql);
        return ps;
    }

    public static String getInsertSql(String table, List<String> columns) {
        String insertSql = "INSERT INTO " + table + "(";
        String placeholder = "?";
        for (String column : columns) {
            insertSql += column + ",";
        }
        insertSql = insertSql.substring(0, insertSql.length() - 1) + ") VALUES (";
        for (int i = 0; i < columns.size(); i++) {
            insertSql += placeholder + ",";
        }
        insertSql = insertSql.substring(0, insertSql.length() - 1) + " ) ";
        return insertSql;
    }

    public static String getInsertSqlFormat(String table, List<String> columns) {
        String insertSql = "INSERT INTO " + table + "(";
        String placeholder = "%s";
        for (String column : columns) {
            insertSql += column + ",";
        }
        insertSql = insertSql.substring(0, insertSql.length() - 1) + ") VALUES (";
        for (int i = 0; i < columns.size(); i++) {
            insertSql += placeholder + ",";
        }
        insertSql = insertSql.substring(0, insertSql.length() - 1) + " ) ";
        return insertSql;
    }

    public static void write(Connection connection, PreparedStatement ps, List<List<Object>> rs) throws SQLException {
        if (rs.isEmpty()) {
            return;
        }
        connection.setAutoCommit(false);
        try {
            for (List<Object> r : rs) {
                for (int i = 0; i < r.size(); i++) {
                    ps.setObject(i + 1, r.get(i));
                }
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            log.error("批量导数据异常【{}】", e.getMessage());
//            throw new SQLException(e);
        }
        connection.commit();
    }
}
