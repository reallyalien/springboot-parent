package com.ot.springboot.mail.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Mail implements Serializable {

    /**
     * 邮件接收方，可多人
     */
    private String[] tos;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
}
