package com.ot.flink.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartInfo {

    private String sensorId;//信号灯id
    private Integer count;//通过该信号灯的车的数量
}
