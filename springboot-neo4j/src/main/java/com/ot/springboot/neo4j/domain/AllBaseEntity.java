package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

public abstract class AllBaseEntity {

    /**
     * id 必须为Long类型，而且必须提供(节点和关系都需要)。且要加这个注解。
     * id 由图数据库统一操作，所以不需要setter
     */
    @Id
    @GeneratedValue
    public Long id;

    public Long getId() {
        return id;
    }
}
