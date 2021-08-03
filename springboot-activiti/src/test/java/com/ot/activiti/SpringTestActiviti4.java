package com.ot.activiti;

import com.ot.activiti.entity.Leave;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTestActiviti4 {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;

    /**
     * 部署流程定义
     */
    @Test
    public void deployProcessDefinition() {
        Deployment deploy = repositoryService
                .createDeployment()
                .name("候选人")
                .addClasspathResource("t1/candidate.bpmn") //上传流程定义规则文件
                .deploy();
        System.out.println("部署ID" + deploy.getId());
        System.out.println("部署名称" + deploy.getName());
    }

    /**
     * 查看流程定义信息
     */
    @Test
    public void findDeployDefinition() {
        List<ProcessDefinition> list = repositoryService
                .createProcessDefinitionQuery()//创建流程定义的查询
                .orderByProcessDefinitionVersion().asc()//使用版本升序排列
                .list();//返回结果集
        if (list != null && list.size() > 0) {
            for (ProcessDefinition pd : list) {
                System.out.println("流程定义ID:" + pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称:" + pd.getName());//对应hello.bpmn文件中的name属性值
                System.out.println("流程定义的key:" + pd.getKey());//对应hello.bpmn文件中的id属性值
                System.out.println("流程定义的版本:" + pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件:" + pd.getResourceName());
                System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
                System.out.println("部署对象ID：" + pd.getDeploymentId());
                System.out.println("*********************************************");
            }
        }
    }


    /**
     * 启动流程实例,启动流程实例时设置变量，此时就是全局变量，作用域是整个流程实例
     */
    @Test
    public void startProcessInstance() {
        //流程定义的key，通过key来启动实例
        String processDefinitionKey = "candidate";
        //通过key启动流程实例，过程当中也可以设置其他的流程参数
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        System.out.println("流程实例ID:" + pi.getId());//流程实例ID
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID
    }

    /**
     * 查询个人任务,任务执行到哪一步，才能查询到，执行不到的步骤查询不到
     */
    @Test
    public void findPersonTask() {
        String assignee = "张三";
        String candidate = "王五";
        String key = "candidate";
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)
//                .taskAssignee(assignee) //指定办理人
                .taskCandidateUser(candidate) //指定候选人查询
                .list();
        if (null != list && !list.isEmpty()) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("任务是否暂停:" + task.isSuspended());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("********************************************");
            }
        }
    }

    /**
     * 查询候选人列表
     */
    @Test
    public void findCandidateUsers(){
        String taskId="20005";
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        if (!identityLinksForTask.isEmpty()){
            for (IdentityLink identityLink : identityLinksForTask) {
                System.out.println(identityLink.getUserId());
            }
        }
        List<Task> 赵六 = taskService.createTaskQuery().taskCandidateUser("赵六").list();
    }

    /**
     * 完成我的任务 如果任务已暂停，则抛出异常  org.activiti.engine.ActivitiException: Cannot complete a suspended task
     *
     * 任务办理时设置流程变量
     * taskService.setVariable(); 必须是当前正在执行的任务才可以设置，任务结束设置，抛出异常
     */
    @Test
    public void completePersonTask() {
        String assignee = "小白脸111";
        String key = "leave";
        Map<String, Object> map = new HashMap<>();
        Leave leave = new Leave();
//        leave.setNum(1D);
        map.put("leave", leave);
        //根据流程的key和负责人查询任务
        Task task = taskService.createTaskQuery().processDefinitionKey(key).taskAssignee(assignee).singleResult();
//        Task task = taskService.createTaskQuery().processDefinitionKey(key).taskId("5002").singleResult();

        //执行任务
        Optional.ofNullable(task).ifPresent(task1 -> taskService.complete(task1.getId(),map));
    }

    /**
     * 查询流程状态，判断我们当前的流程进行到哪一步 ,
     */
    @Test
    public void findProcessState() {
        String key = "myProcess_1";
        List<Execution> list = runtimeService.createExecutionQuery().processDefinitionKey(key).list();
//        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();
//        if (execution == null) {
//            System.out.println("流程已经结束");
//        } else {
//            System.out.println("流程尚未结束");
//            //获取任务状态
//            System.out.println(execution.getActivityId());
//        }
        if (null != list && !list.isEmpty()) {
            System.out.println("流程尚未结束");
            System.out.println("获取当前任务执行状态");
            for (Execution execution : list) {
                System.out.println(execution.getActivityId());
            }
        } else {
            System.out.println("流程已结束");
        }
    }

    /**
     * 查询流程实例状态，判断我们当前的流程进行到哪一步 ,
     */
    @Test
    public void findProcessState1() {
        String key = "myProcess_1";
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey(key).list();
        for (ProcessInstance processInstance : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id："
                    + processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id："
                    + processInstance.getProcessDefinitionId());
            System.out.println("是否执行完成：" + processInstance.isEnded());
            System.out.println("业务key：" + processInstance.getBusinessKey());
            System.out.println("是否暂停：" + processInstance.isSuspended());
            System.out.println("当前活动标识：" + processInstance.getActivityId());
        }
    }


}
