package com.springboot.jta.atomikos.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContext.context.get();
    }

    static class DataSourceContext {
        private static final ThreadLocal<String> context = new ThreadLocal<String>();

        public void setContext(String key) {
            context.set(key);
        }

        public String getContext() {
            return context.get();
        }

        public void clear() {
            context.remove();
        }
    }
}
