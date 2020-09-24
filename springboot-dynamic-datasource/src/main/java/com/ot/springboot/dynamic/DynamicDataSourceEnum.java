package com.ot.springboot.dynamic;

public enum DynamicDataSourceEnum {
    MASTER("master"),
    SLAVE("slave");
    private String dataSourceName;

    DynamicDataSourceEnum(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }
    public String getDataSourceName(){
        return this.dataSourceName;
    }

}
