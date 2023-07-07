package com.ot.flink;

import com.ot.flink.entity.LoginEvent;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class CEPTest extends BaseTest {

    /**
     * 接下来我们考虑一个具体的需求：检测用户行为，如果连续三次登录失败，就输出报警信
     * 息。很显然，这是一个复杂事件的检测处理，我们可以使用 Flink CEP 来实现。
     * 我们首先定义数据的类型。这里的用户行为不再是之前的访问事件 Event 了，所以应该单
     * 独定义一个登录事件 POJO 类。具体实现如下
     * <p>
     * 接下来就是业务逻辑的编写。Flink CEP 在代码中主要通过 Pattern API 来实现。之前我们
     * 已经介绍过，CEP 的主要处理流程分为三步，对应到 Pattern API 中就是：
     * （1）定义一个模式（Pattern）；
     * （2）将Pattern应用到DataStream上，检测满足规则的复杂事件，得到一个PatternStream；
     * （3）对 PatternStream 进行转换处理，将检测到的复杂事件提取出来，包装成报警信息输
     * 出
     */
    @Test
    public void a() {
        env.setParallelism(1);
        KeyedStream<LoginEvent, String> keyedStream = env
                .fromElements(
                        new LoginEvent("user_1", "192.168.0.1", "fail", 2000L),
                        new LoginEvent("user_1", "192.168.0.2", "fail", 3000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 4000L),
                        new LoginEvent("user_1", "171.56.23.10", "fail", 5000L),
                        new LoginEvent("user_2", "192.168.1.29", "success", 6000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 7000L),
                        new LoginEvent("user_2", "192.168.1.29", "fail", 8000L)

                ).assignTimestampsAndWatermarks(WatermarkStrategy.<LoginEvent>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<LoginEvent>() {
                            @Override
                            public long extractTimestamp(LoginEvent element, long recordTimestamp) {
                                return element.timestamp;
                            }
                        }))
//                .keyBy(k -> k.userId);
                .keyBy(new KeySelector<LoginEvent, String>() {
                    @Override
                    public String getKey(LoginEvent value) throws Exception {
                        return value.userId;
                    }
                });

        //定义pattern，连续的三个登录失败事件
        Pattern<LoginEvent, LoginEvent> eventPattern = Pattern
                .<LoginEvent>begin("first")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent value) throws Exception {
                        return value.eventType.equals("fail");
                    }

                })
                .next("second")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent value) throws Exception {
                        return value.eventType.equals("fail");
                    }
                })
                .next("third")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent value) throws Exception {
                        return value.eventType.equals("fail");
                    }
                });
        //将pattern应用到流上，检测匹配的复杂事件，得到一个patternStream
        PatternStream<LoginEvent> patternStream = CEP.pattern(keyedStream, eventPattern);

        //将匹配到的复杂事件选择出来，然后包装成字符串信息输出
        patternStream.select(new PatternSelectFunction<LoginEvent, String>() {
            @Override
            public String select(Map<String, List<LoginEvent>> map) throws Exception {
                System.out.println(map);
                LoginEvent first = map.get("first").get(0);
                LoginEvent second = map.get("second").get(0);
                LoginEvent third = map.get("third").get(0);
                return first.userId + " 连续三次登录失败！登录时间：" +
                        first.timestamp + ", " + second.timestamp + ", " + third.timestamp;
            }
        }).print("warning");
    }
}
