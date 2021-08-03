package com.ot.activiti.listener;

import org.activiti.bpmn.model.EventListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * Create：任务创建后触发
 * Assignment：任务分配后触发
 * Delete：任务完成后触发
 * All：所有事件发生都触发
 */

public class MyEventListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getName().equals("出差申请") && delegateTask.getEventName().equals("create")) {
            //指定负责人
            delegateTask.setAssignee("张三111");
        }
    }
}
