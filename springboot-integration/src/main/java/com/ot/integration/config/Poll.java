package com.ot.integration.config;

import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.stereotype.Component;

/**
 * 队列channel使用的是pollConsumer，其他的使用的是eventDrivenConsumer，事件驱动消费者
 */
@Component
public class Poll extends PollerMetadata {
}
