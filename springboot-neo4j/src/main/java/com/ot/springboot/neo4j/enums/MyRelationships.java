package com.ot.springboot.neo4j.enums;

import org.neo4j.graphdb.RelationshipType;

/**
 * 关系类型枚举
 */
public enum MyRelationships implements RelationshipType {
    JVM_LANGIAGES, NON_JVM_LANGIAGES;
}
