package com.ot.flink.entity;


import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Event {

    public String user;
    public String url;
    public Long timestamp;
}
