package com.ot.springboot.neo4j.enums;

import org.neo4j.graphdb.Label;

/**
 * 标签类型枚举
 */
public enum MyLabel implements Label {
    JAVA, SCALA, SQL, NEO4J;
}
