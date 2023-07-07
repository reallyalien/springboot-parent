package com.ot.flink.waterremark;

import com.ot.flink.entity.Event;
import org.apache.flink.api.common.eventtime.Watermark;
import org.apache.flink.api.common.eventtime.WatermarkGenerator;
import org.apache.flink.api.common.eventtime.WatermarkOutput;

/**
 * 自定义周期性水位线
 */
public class CustomPeriodicGenerator implements WatermarkGenerator<Event> {

    private Long delayTime = 5000L; // 延迟时间
    private Long maxTs = Long.MIN_VALUE + delayTime + 1L; // 观察到的最大时间戳

    /**
     * 每个事件到来之后调用，断点式生成
     *
     * @param event
     * @param eventTimestamp
     * @param output
     */
    @Override
    public void onEvent(Event event, long eventTimestamp, WatermarkOutput output) {
        //数据每来一次，就更新最大时间戳
        maxTs = Math.max(event.timestamp, maxTs);
    }

    /**
     * 由框架周期调用，周期性生成
     *
     * @param output
     */
    @Override
    public void onPeriodicEmit(WatermarkOutput output) {
        //发射水位线，默认200ms调用一次
        output.emitWatermark(new Watermark(maxTs - delayTime - 1));
    }
}
