package com.lowcode.workflow.runner.runtime;


import com.lowcode.workflow.common.model.NodeRuntimeData;
import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.enu.NodeTypeEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeRuntimeDataRunning {


    public void runNodeRuntimeData(List<NodeRuntimeData> nodeRuntimeDataList) {
        String flowId = nodeRuntimeDataList.get(0).getFlowId();
        // 获取所有开始节点
        List<NodeRuntimeData> start = CollectionUtil.filter(nodeRuntimeDataList, this::isStart);
//        System.out.println("----------------(测试)收集的所有开始节点----------------");
//        start.forEach(System.out::println);
        // 调用线程池执行所有的开始节点
        CollectionUtil.foreach(start, node -> RuntimeDataThreadPool.getThreadPool(flowId).execute(() -> new RuntimeDataExecutor(nodeRuntimeDataList).start(node)));
    }


    private boolean isStart(NodeRuntimeData nodeRuntimeData) {
        return NodeTypeEnum.START.getType().equals(nodeRuntimeData.getType());
    }
}
