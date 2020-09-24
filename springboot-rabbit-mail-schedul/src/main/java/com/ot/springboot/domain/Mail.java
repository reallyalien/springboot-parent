package com.ot.springboot.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Mail implements Serializable {

    private String to;
    private String title;
    private String content;
    private String msgId;

}
