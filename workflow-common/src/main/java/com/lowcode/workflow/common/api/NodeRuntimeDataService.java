package com.lowcode.workflow.common.api;

import com.lowcode.workflow.common.model.NodeRuntimeData;

import java.util.List;

public interface NodeRuntimeDataService {

    /**
     * 将构建的节点运行时数据提交运行
     * @param nodeRuntimeDataList 运行时数据
     */
    void runNodeRuntimeData(List<NodeRuntimeData> nodeRuntimeDataList);





}
