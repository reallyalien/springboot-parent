package com.ot.springboot.neo4j.domain;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity
public class SupplyRelationship {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @StartNode
    private Company company;

    @EndNode
    private Supply supply;

    /**
     * 采购占比
     */
    private String scale;

    /**
     * 采购金额
     */
    private String amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Supply getSupply() {
        return supply;
    }

    public void setSupply(Supply supply) {
        this.supply = supply;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SupplyRelationship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company=" + company +
                ", supply=" + supply +
                ", scale='" + scale + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
