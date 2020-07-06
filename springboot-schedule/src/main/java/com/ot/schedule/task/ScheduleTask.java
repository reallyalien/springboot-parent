package com.ot.schedule.task;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 创建定时任务对象需要实现SchedulingConfigurer
 */
@Data
@Component
public class ScheduleTask implements SchedulingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);
    private String cron = "* * * * * ?";
    private String name = "测试";

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        scheduledTaskRegistrar.addCronTask();//每到指定时间触发
        scheduledTaskRegistrar.addTriggerTask(doTask(), getTrigger());//每隔指定时间触发
    }

    /**
     * 业务执行的方法
     *
     * @return
     */
    public Runnable doTask() {
        return () -> {
            log.info("name:" + name + ",时间：" + LocalDateTime.now());
        };
    }

    /**
     * 业务触发器
     *
     * @return
     */
    public Trigger getTrigger() {
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //触发器
                CronTrigger cronTrigger = new CronTrigger(cron);
                return cronTrigger.nextExecutionTime(triggerContext);
            }
        };
    }

}
