package com.ot.flink.entity;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UrlViewCount {

    public String url;
    public Long count;
    public Long windowStart;
    public Long windowEnd;
}
