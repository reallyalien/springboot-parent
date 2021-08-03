package com.ot.activiti;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTestActiviti {

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
                .name("出差审批")
                .addClasspathResource("bpmn/evection.bpmn") //上传流程定义规则文件
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        System.out.println("部署ID" + deploy.getId());
        System.out.println("部署名称" + deploy.getName());
    }

    /**
     * 部署流程定义二 从zip文件当中
     */
    @Test
    public void deploymentProcessDefinition_zip() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("processes/hello.zip");
        ZipInputStream zipInputStream = new ZipInputStream(is);
        Deployment deploy = repositoryService
                .createDeployment()
                .name("流程定义")
                .addZipInputStream(zipInputStream)
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
//                .deploymentId("") //查询条件
//                .processDefinitionId("")
//                .processDefinitionKey("")
//                .processDefinitionNameLike("")
                .orderByProcessDefinitionVersion().asc()//使用版本升序排列
                .list();//返回结果集
//                      .singleResult();//返回惟一结果集
//                      .count();//返回结果集数量
//                      .listPage(firstResult, maxResults);//分页查询
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
     * 删除流程定义信息
     */
    @Test
    public void deleteProcessDefinition() {
        //使用部署ID完成删除
        String deployId = "25002";
        //非级联删除，只能删除没有启动的流程，如果流程启动就会抛出异常
        repositoryService.deleteDeployment(deployId);
        //级联删除，不管流程是否启动，都能删除
//        repositoryService.deleteDeployment(deployId,true);

    }

    /**
     * 获取流程定义的文档资源
     */
    @Test
    public void viewPic() throws IOException {
        String deployId = "1";
        //获取资源名称 [processes/hello.bpmn, processes/hello.png]
        List<String> resourceNames = repositoryService.getDeploymentResourceNames(deployId);
        List<String> collect = resourceNames.stream()
                .map(String::toLowerCase)
                .filter(m -> m.endsWith(".png"))
                .collect(Collectors.toList());
        String pngResourceName = collect.get(0);
        //获取图片的输入流
        InputStream is = repositoryService.getResourceAsStream(deployId, pngResourceName);
        //写入到本地
        File file = new File("d:/" + "1.png");
        FileCopyUtils.copy(is, new FileOutputStream(file));
    }

    /**
     * 查询最新版本的流程定义信息
     */
    @Test
    public void findLastVersionProcessDefinition() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        Map<String, ProcessDefinition> map = new LinkedHashMap<>();
        if (null != list && !list.isEmpty()) {
            for (ProcessDefinition processDefinition : list) {
                map.put(processDefinition.getKey(), processDefinition);
            }
        }
        List<ProcessDefinition> last = new ArrayList<>(map.values());
        if (!last.isEmpty()) {
            for (ProcessDefinition pd : last) {
                System.out.println("流程定义ID:" + pd.getId());//流程定义的key+版本+随机生成数
                System.out.println("流程定义的名称:" + pd.getName());//对应hello.bpmn文件中的name属性值
                System.out.println("流程定义的key:" + pd.getKey());//对应hello.bpmn文件中的id属性值
                System.out.println("流程定义的版本:" + pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
                System.out.println("资源名称bpmn文件:" + pd.getResourceName());
                System.out.println("资源名称png文件:" + pd.getDiagramResourceName());
                System.out.println("部署对象ID：" + pd.getDeploymentId());
                System.out.println("*********************************************************************************");
            }
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        //流程定义的key，通过key来启动实例
        String processDefinitionKey = "evection";
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
        String assignee = "zhangsan";
//        String assignee = "jerry";
//        String assignee = "jack";
//        String assignee = "rose";
        String key = "evection";
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee) //指定办理人
//                      .taskCandidateUser(candidateUser)//组任务的办理人查询
//                      .processDefinitionId(processDefinitionId)//使用流程定义ID查询
//                      .processInstanceId(processInstanceId)//使用流程实例ID查询
//                      .executionId(executionId)//使用执行对象ID查询
                .orderByTaskCreateTime()
                .asc()
                .list();
//                      .singleResult()//返回惟一结果集
//                      .count()//返回结果集的数量
//                      .listPage(firstResult, maxResults);//分页查询
        if (null != list && !list.isEmpty()) {
            for (Task task : list) {
                System.out.println("任务ID:" + task.getId());
                System.out.println("任务名称:" + task.getName());
                System.out.println("任务的创建时间:" + task.getCreateTime());
                System.out.println("任务的办理人:" + task.getAssignee());
                System.out.println("流程实例ID：" + task.getProcessInstanceId());
                System.out.println("执行对象ID:" + task.getExecutionId());
                System.out.println("流程定义ID:" + task.getProcessDefinitionId());
                System.out.println("********************************************");
            }
        }
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completePersonTask() {
//        String assignee = "zhangsan";
//        String assignee = "jerry";
//        String assignee = "jack";
        String assignee = "rose";
        String key = "evection";
        //根据流程的key和负责人查询任务
        Task task = taskService.createTaskQuery().processDefinitionKey(key).taskAssignee(assignee).singleResult();
        //执行任务
        taskService.complete(task.getId());
    }

    /**
     * 查询流程状态，判断我们当前的流程进行到哪一步 ,
     */
    @Test
    public void findProcessState() {
        String key = "evection";
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
     * 历史活动查询接口
     */
    @Test
    public void findHistoryActivity() {
        String processInstanceId = "2501";
        List<HistoricActivityInstance> list = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (HistoricActivityInstance hai : list) {
            System.out.println("活动id：" + hai.getActivityId()
                    + "   审批人：" + hai.getAssignee()
                    + "   任务id：" + hai.getTaskId());
            System.out.println("************************************");
        }
    }

    /**
     * 历史流程实例查询接口
     */
    @Test
    public void findHistoryProcessInstance() {
        String processInstanceId = "2501";
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByProcessInstanceStartTime().asc()
                .singleResult();
        System.out.println(hpi.getId() + "    " + hpi.getProcessDefinitionId() + "    " + hpi.getStartTime() + "    "
                + hpi.getEndTime() + "     " + hpi.getDurationInMillis());
    }

    /**
     * 历史任务实例查询接口
     */
    @Test
    public void findHistoryTask() {
        String processInstanceId = "2501";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc().list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance hti : list) {
                System.out.println("\n 任务Id：" + hti.getId() + "    任务名称：" + hti.getName() + "    流程实例Id：" + hti.getProcessInstanceId() + "\n 开始时间："
                        + hti.getStartTime() + "   结束时间：" + hti.getEndTime() + "   持续时间：" + hti.getDurationInMillis());
            }
        }
    }

    /**
     * 通过sql来查询历史数据，查询历史本地
     */
    @Test
    public void findHistoryByNative() {
        HistoricProcessInstance result = historyService.createNativeHistoricProcessInstanceQuery()
                .sql("")
                .singleResult();
    }

}
