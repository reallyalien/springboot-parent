package com.ot.springboot.msg;

import lombok.Data;

@Data
public class ReceiveMessage {
    private String msg;
    private String user;
}
