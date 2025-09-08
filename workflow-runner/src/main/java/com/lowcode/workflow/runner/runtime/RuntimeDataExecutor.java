package com.lowcode.workflow.runner.runtime;


import com.lowcode.workflow.common.model.NodeRuntimeData;

import java.util.List;

/**
 * 单个流程的执行器
 */
public class RuntimeDataExecutor {

    private final List<NodeRuntimeData> nodeRuntimeDataList;

    private final String flowId;

    public RuntimeDataExecutor(List<NodeRuntimeData> nodeRuntimeDataList) {
        this.nodeRuntimeDataList = nodeRuntimeDataList;
        this.flowId = nodeRuntimeDataList.get(0).getFlowId();
    }

    /**
     * 执行节点
     * @param nodeRuntimeData 节点运行时数据
     */
    public void run(NodeRuntimeData nodeRuntimeData) {

    }






}
