package com.ot.activiti;

import com.ot.activiti.entity.Leave;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.parser.BpmnShapeParser;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.bpmn.behavior.BpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.validation.validator.impl.SequenceflowValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTestActiviti5 {

    private static Logger logger = LoggerFactory.getLogger(SpringTestActiviti5.class);

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

    @Test
    public void testDynamicDeploy() {
        //创建bpmn模型
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        process.setId(UUID.randomUUID().toString());
        bpmnModel.addProcess(process);
        //创建bpmn元素
        process.addFlowElement(createStartEvent());
        process.addFlowElement(createUserTask("task1", "first task", "fred"));
        process.addFlowElement(createEndEvent());
        //连线
        process.addFlowElement(createSequenceFlow("start", "task1"));
        process.addFlowElement(createSequenceFlow("task1", "end"));

        //生成布局
        BpmnAutoLayout autoLayout = new BpmnAutoLayout(bpmnModel);
        autoLayout.execute();

        //部署
        Deployment deploy = repositoryService.createDeployment()
                .addBpmnModel("dynamic-model.bpmn", bpmnModel)
                .name("Dynamic process deployment")
                .deploy();
        //保存流程图
        savePic(deploy.getId());

    }

    @Test
    public void testDynamicDeployGateway() {
        //创建bpmn模型
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        process.setId("dyn1111");
        bpmnModel.addProcess(process);
        //创建bpmn元素
        process.addFlowElement(createStartEvent());
        process.addFlowElement(createUserTask("task1", "开始签名", "fred"));
        process.addFlowElement(createParallelGateway("gw1", "并行网关1"));
        process.addFlowElement(createUserTask("task2", "A签名", "A"));
        process.addFlowElement(createUserTask("task3", "B签名", "B"));
        process.addFlowElement(createParallelGateway("gw2", "并行网关2"));
        process.addFlowElement(createEndEvent());
        //连线
        process.addFlowElement(createSequenceFlow("start", "task1"));
        process.addFlowElement(createSequenceFlow("task1", "gw1"));
        process.addFlowElement(createSequenceFlow("gw1", "task2"));
        process.addFlowElement(createSequenceFlow("gw1", "task3"));
        process.addFlowElement(createSequenceFlow("task2", "gw2"));
        process.addFlowElement(createSequenceFlow("task3", "gw2"));
        process.addFlowElement(createSequenceFlow("gw2", "end"));

        //生成布局
        BpmnAutoLayout autoLayout = new BpmnAutoLayout(bpmnModel);
        autoLayout.execute();

        //部署
        Deployment deploy = repositoryService.createDeployment()
                .addBpmnModel("dynamic-model.bpmn", bpmnModel)
                .name("Dynamic process deployment")
                .deploy();
        //保存流程图
        savePic(deploy.getId());

    }

    @Test
    public void testDynamicDeployGatewayWithParams() {
        List<List<String>> listList = new ArrayList<>();

        List<String> list1 = Arrays.asList("空值替换");

        listList.add(list1);
        //创建bpmn模型
        BpmnModel bpmnModel = new BpmnModel();
        Process process = new Process();
        process.setId("的1111");
        bpmnModel.addProcess(process);
        //创建bpmn元素
        process.addFlowElement(createStartEvent());
        for (int i = 0; i < listList.size(); i++) {
            List<String> list = listList.get(i);
            String head = "h" + i;
            String tail = "t" + i;
            process.addFlowElement(createParallelGateway(head, head));
            process.addFlowElement(createParallelGateway(tail, tail));
            if (i == 0) {
                process.addFlowElement(createSequenceFlow("start", head));
            } else {
                process.addFlowElement(createSequenceFlow("t" + (i - 1), head));
                if (i == listList.size() - 1) {
                    process.addFlowElement(createSequenceFlow(tail, "end"));
                }
            }

            for (int j = 0; j < list.size(); j++) {
                String str = list.get(j);
                String userTaskId = String.valueOf(str);
                process.addFlowElement(createUserTask(userTaskId, userTaskId, userTaskId));
                process.addFlowElement(createSequenceFlow(head, userTaskId));
                process.addFlowElement(createSequenceFlow(userTaskId, tail));
            }

        }

        process.addFlowElement(createEndEvent());


        //生成布局
        BpmnAutoLayout autoLayout = new BpmnAutoLayout(bpmnModel);
        autoLayout.execute();

        //部署
        Deployment deploy = repositoryService.createDeployment()
                .addBpmnModel("dynamic-model.bpmn", bpmnModel)
                .name("Dynamic process deployment")
                .deploy();
        //保存流程图
        savePic(deploy.getId());

    }

    public void savePic(String deployId) {
        List<String> deploymentResourceNames = repositoryService.getDeploymentResourceNames(deployId);
        List<String> collect = deploymentResourceNames.stream().filter(name -> name.endsWith(".bpmn")).collect(Collectors.toList());
        String resourceName = null;
        if (!collect.isEmpty()) {
            resourceName = collect.get(0);
        }
        String finalResourceName = resourceName;
        Optional.ofNullable(resourceName).ifPresent(s -> {
            InputStream is = repositoryService.getResourceAsStream(deployId, finalResourceName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("d:/1.xml");
                FileCopyUtils.copy(is, fos);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }
        });
    }

    //创建箭头
    private SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    //创建task
    protected UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
    }

    //创建并行网关
    private Gateway createParallelGateway(String id, String name) {
        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(id);
        parallelGateway.setName(name);
        return parallelGateway;
    }

    //创建开始节点
    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    //创建结束节点
    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }

    //====================================================================================

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
        String processDefinitionKey = "的1111";
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
        String assignee = "_1";
        String key = "dyn1111";
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assignee) //指定办理人
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
    public void findCandidateUsers() {
        String taskId = "20005";
        List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
        if (!identityLinksForTask.isEmpty()) {
            for (IdentityLink identityLink : identityLinksForTask) {
                System.out.println(identityLink.getUserId());
            }
        }
        List<Task> 赵六 = taskService.createTaskQuery().taskCandidateUser("赵六").list();
    }

    /**
     * 完成我的任务 如果任务已暂停，则抛出异常  org.activiti.engine.ActivitiException: Cannot complete a suspended task
     * <p>
     * 任务办理时设置流程变量
     * taskService.setVariable(); 必须是当前正在执行的任务才可以设置，任务结束设置，抛出异常
     */
    @Test
    public void completePersonTask() {
        String assignee = "_6";
        String key = "dyn1111";
        //根据流程的key和负责人查询任务
        Task task = taskService.createTaskQuery().processDefinitionKey(key).taskAssignee(assignee).singleResult();
//        Task task = taskService.createTaskQuery().processDefinitionKey(key).taskId("5002").singleResult();

        //执行任务
        Optional.ofNullable(task).ifPresent(task1 -> taskService.complete(task1.getId()));
    }

    /**
     * 查询流程状态，判断我们当前的流程进行到哪一步 ,
     */
    @Test
    public void findProcessState() {
        String key = "eb301254-d309-4e74-ad4d-57ec77d21801";
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

    @Test
    public void a() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("的1111")
                .processInstanceId("2501")
                .singleResult();
        System.out.println(processInstance);
    }

    @Test
    public void b() {
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey("的1111")
                .processInstanceId("2501")
                .singleResult();
        System.out.println(processInstance);
    }

    @Test
    public void c() {
        taskService.complete("2506");
    }

    @Test
    public void d() {
        runtimeService.deleteProcessInstance("2501", "sssss");
    }

    @Test
    public void e() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("1", 0);
        logger.info("map:{}", map.values());
    }

    @Test
    public void f() {
        List<Execution> list = runtimeService.createExecutionQuery()
                .processDefinitionKey("的1111")
                .processInstanceId("10001")
                .list();
        List<String> result = null;
    }

    @Test
    public void g() {
        HistoricProcessInstance result = historyService.createHistoricProcessInstanceQuery()
               .deploymentId("7501").singleResult();
        System.out.println(result);
    }
    @Test
    public void h(){
        repositoryService.deleteDeployment("7501",true);
    }
}
