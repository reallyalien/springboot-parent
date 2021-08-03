package com.ot.activiti;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestActiviti {

    private static Logger logger = LoggerFactory.getLogger(TestActiviti.class);

    /**
     * 使用代码创建工作流使用的25张表
     */
    @Test
    public void createTable() {
        //创建引擎配置类
        ProcessEngineConfiguration conf = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        conf.setDatabaseType("mysql");
        conf.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        conf.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti");
        conf.setJdbcUsername("root");
        conf.setJdbcPassword("root");
        //不自动创建表，需要表存在 DB_SCHEMA_UPDATE_FALSE = "false";
        //先删除表，再创建表 DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
        //如果表不存在，先创建表 DB_SCHEMA_UPDATE_TRUE = "true";
        conf.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        //创建工作流核心对象
        ProcessEngine processEngine = conf.buildProcessEngine();
//        processEngine.close();
        System.out.println(processEngine);
    }

    /**
     * 此方法会自动寻找当前类路径下的  activiti.cfg.xml 文件
     */
    @Test
    public void createTable1() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(engine);
    }

    /**
     * ProcessEngine 工作流当中最核心的类，其他的类都是由它而来，由工作引擎可以创建各个service，这些service调用所有的表
     */
    @Test
    public void getService() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        //管理流程定义,bpmn文件和流程图片
        //=============================================================
        RepositoryService repositoryService = engine.getRepositoryService();
        //可产生DeploymentBuilder用来定义流程部署的相关参数
        DeploymentBuilder deployment = repositoryService.createDeployment();
        //删除流程定义
        repositoryService.deleteDeployment("");
        //==============================================================
        //执行管理，包括启动，推进和删除流程实例等
        RuntimeService runtimeService = engine.getRuntimeService();
        //=============================================================
        //任务管理
        TaskService taskService = engine.getTaskService();
        //=============================================================
        //历史管理（执行完数据的管理）
        HistoryService historyService = engine.getHistoryService();
        //=============================================================
        //组织机构管理
        ManagementService managementService = engine.getManagementService();
    }

    /**
     * 部署流程定义
     */
    @Test
    public void deploymentProcessDefinition() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        Deployment deployment = repositoryService
                .createDeployment()                              //创建部署对象
                .name("hello入门程序")                           //名称
                .addClasspathResource("diagrams/hello.bpmn")     //加载资源路径
                .deploy();                                       //完成部署
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    /**
     * https://www.cnblogs.com/runtimeexception/p/8961395.html
     */

    /**
     * 启动流程定义
     */
    @Test
    public void startProcessInstance() {
        //获取与正在执行的流程示例和执行对象相关的service
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        //使用流程定义的key启动实例,key对应bpmn文件中id的属性值，默认按照最新版本流程启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1");
        System.out.println(processInstance.getId());
        System.out.println(processInstance.getName());
    }

    /**
     * 查看我的个人任务
     */
    @Test
    public void findPersonTask() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        List<Task> list = taskService
                .createTaskQuery() //创建查询任务对象
                .taskAssignee("") //指定个人任务查询，指定办理人
                .list();
        if (null != list && !list.isEmpty()) {
            for (Task task : list) {
                System.out.println(task.getId());
                System.out.println(task.getName());
                System.out.println(task.getCreateTime());
                System.out.println(task.getAssignee());
                System.out.println(task.getProcessInstanceId());
                System.out.println(task.getExecutionId());
                System.out.println(task.getProcessDefinitionId());
            }
        }
    }

    @Test
    public void test1() {
        Map<String, Integer> map = new HashMap<>();

        logger.info("map:{}",map);
    }
}
