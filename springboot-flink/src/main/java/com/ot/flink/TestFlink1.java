package com.ot.flink;

import com.ot.flink.entity.Event;
import com.ot.flink.source.ClickSource;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.metrics.Gauge;
import org.apache.flink.metrics.MeterView;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TestFlink1 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Event> eventStream = env.addSource(new ClickSource());
        eventStream.map(new RichMapFunction<Event, Event>() {
            @Override
            public Event map(Event value) throws Exception {
                this.counter.inc();
                this.meter.markEvent();
                this.meter.update();
                System.out.println(this.meter.getCount() + "-------" +this.meter.getRate());
                return value;
            }

            private static final long serialVersionUID = 6417761179003349645L;
            private transient Counter counter;
            private transient Gauge<String> gauge;
            private transient MeterView meter;

            @Override
            public void open(Configuration parameters) throws Exception {
                MetricGroup metricGroup = getRuntimeContext().getMetricGroup();
                this.counter = metricGroup.counter("aaacount");
                this.gauge = metricGroup.gauge("aaagauge", new Gauge<String>() {
                    @Override
                    public String getValue() {
                        return 111 + "";
                    }
                });
                this.meter = metricGroup.meter("aaarate", new MeterView(10));
            }

            @Override
            public void close() throws Exception {
                System.out.println(this.counter.getCount());
                System.out.println(this.meter.getRate());
            }
        }).print();
        env.execute("我的测试");
    }
}
