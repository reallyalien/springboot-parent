//package com.ot.activiti;
//
//import org.activiti.bpmn.BpmnAutoLayout;
//import org.activiti.bpmn.model.Process;
//import org.activiti.bpmn.model.*;
//import org.activiti.engine.*;
//import org.activiti.engine.history.HistoricActivityInstance;
//import org.activiti.engine.history.HistoricProcessInstance;
//import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.repository.ProcessDefinition;
//import org.activiti.engine.runtime.Execution;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.IdentityLink;
//import org.activiti.engine.task.Task;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.FileCopyUtils;
//
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SpringTestActiviti6 {
//
//    @Autowired
//    private ProcessEngine processEngine;
//
//    @Autowired
//    private RepositoryService repositoryService;
//    @Autowired
//    private TaskService taskService;
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private HistoryService historyService;
//    /*findHistoryProcessInstanceByBusKey方法*/
//    /**
//     * 根据流程businessKey查询历史流程实例
//     * @param processId
//     * @return
//     */
//    public HistoricProcessInstance findHistoryProcessInstanceByBusKey(String businessKey){
//        return historyService.createHistoricProcessInstanceQuery()
//                .processInstanceBusinessKey(businessKey).singleResult();
//    }
//
//    /*getFlowMap方法*/
//    public InputStream getFlowMap(HistoricProcessInstance processInstance, String instanceId, String flowType) {
//
//        // RuntimeService runtimeService = processEngine.getRuntimeService();
//        // DynamicBpmnService flowMoniService = processEngine.getDynamicBpmnService();
//
//        /*资源服务*/
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        /*历史数据服务*/
//        HistoryService historyService = processEngine.getHistoryService();
//        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
//
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//
//        /*为了流程监控图显示效果，替换多有未取到的变量，只显示节点的中文描述*/
//        List<ActivityImpl> activityList = processDefinition.getActivities();
//        for(ActivityImpl activity : activityList){
//            String name = bpmnModel.getFlowElement(activity.getId()).getName();
//            bpmnModel.getFlowElement(activity.getId())
//                    .setName(name.replaceAll("[\\w{}$\\-+]", ""));
//        }
//
//        /*历史节点，取出变量，设置为节点的名称*/
//        List<ArkHistoricActivity> hisList = findProcessHistoryByPiid(instanceId);
//        for(ArkHistoricActivity hisActivity : hisList){
//            bpmnModel.getFlowElement(hisActivity.getActivityId())
//                    .setName(hisActivity.getActivityName());
//        }
//
//        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
//                .processInstanceId(instanceId).orderByHistoricActivityInstanceStartTime().asc().list();
//        List<String> activitiIds = new ArrayList<String>();
//        List<String> flowIds = new ArrayList<String>();
//
//        /*获取流程走过的线*/
//        flowIds = flowServiceImpl.getHighLightedFlows(processDefinition, activityInstances);
//
//        /*获取流程走过的节点*/
//        for (HistoricActivityInstance hai : activityInstances) {
//            activitiIds.add(hai.getActivityId());
//        }
//        Context.setProcessEngineConfiguration(
//                (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration());
//
//        /**
//         * 从配置文件中获取中文配置信息，避免中文乱码
//         * processEngine.getProcessEngineConfiguration().getActivityFontName(),
//         * processEngine.getProcessEngineConfiguration().getLabelFontName(),
//         */
//        InputStream imageStream = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", activitiIds,
//                flowIds, processEngine.getProcessEngineConfiguration().getActivityFontName(),
//                processEngine.getProcessEngineConfiguration().getLabelFontName(),
//                "", null, 1.0);
//
//        return imageStream;
//    }
//
//
//    /*getHighLightedFlows方法*/
//    public List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
//                                            List<HistoricActivityInstance> historicActivityInstances) {
//
//        /*用以保存高亮的线flowId*/
//        List<String> highFlows = new ArrayList<String>();
//        /*对历史流程节点进行遍历*/
//        for (int i = 0; i < historicActivityInstances.size() - 1; i++) {
//            /*得到节点定义的详细信息*/
//            ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());
//            /*用以保存后需开始时间相同的节点*/
//            List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();
//            /*将后面第一个节点放在时间相同节点的集合里*/
//            ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
//            sameStartTimeNodes.add(sameActivityImpl1);
//
//            for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
//                /*后续第一个节点*/
//                HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);
//                /*后续第二个节点*/
//                HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);
//                /*如果第一个节点和第二个节点开始时间相同保存*/
//                if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
//                    ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
//                    sameStartTimeNodes.add(sameActivityImpl2);
//                }
//                /*有不相同跳出循环*/
//                else {
//                    break;
//                }
//            }
//            /*取出节点的所有出去的线*/
//            List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
//            /*对所有的线进行遍历*/
//            for (PvmTransition pvmTransition : pvmTransitions) {
//                /*如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示*/
//                ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
//                if (sameStartTimeNodes.contains(pvmActivityImpl)) {
//                    highFlows.add(pvmTransition.getId());
//                }
//            }
//        }
//        return highFlows;
//    }
//
//}
