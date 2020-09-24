package com.ot.springboot.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * 自定义动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 确定当前查找的键,基于key查找对应的数据源，
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.get();
    }
}
