package com.ot.springboot.jpa.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "StringTest")
public class StringTest implements Serializable {


    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column
    private byte[] data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
