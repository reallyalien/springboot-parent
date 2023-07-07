package com.ot.flink.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginEvent {
    public String userId;
    public String ipAddress;
    public String eventType;
    public Long timestamp;
}
