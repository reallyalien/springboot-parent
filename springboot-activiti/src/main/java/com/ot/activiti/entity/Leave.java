package com.ot.activiti.entity;


import java.io.Serializable;

public class Leave implements Serializable {

    private static final long serialVersionUID = -4672427687941292499L;

    /**
     * 主键id
     */
    private String id;
    /**
     * 请假天数
     */
    private Double num;
    /**
     * 申请人
     */
    private String leaver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public String getLeaver() {
        return leaver;
    }

    public void setLeaver(String leaver) {
        this.leaver = leaver;
    }

    @Override
    public String toString() {
        return "Leave{" +
                "id=" + id +
                ", num=" + num +
                ", leaver=" + leaver +
                '}';
    }
}
