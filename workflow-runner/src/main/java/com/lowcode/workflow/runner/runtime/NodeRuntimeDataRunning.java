package com.lowcode.workflow.runner.runtime;


import com.lowcode.workflow.common.model.NodeRuntimeData;
import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.enu.NodeTypeEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeRuntimeDataRunning {


    public void runNodeRuntimeData(List<NodeRuntimeData> nodeRuntimeDataList) {
        // 获取所有开始节点
        List<NodeRuntimeData> start = CollectionUtil.filter(nodeRuntimeDataList, this::isStart);
        // 获取所有作为开始节点的定时器节点
//        CollectionUtil.filter(nodeRuntimeDataList, )
        // 调用线程池执行所有的开始节点

    }


    private boolean isStart(NodeRuntimeData nodeRuntimeData) {
        return NodeTypeEnum.START.getType().equals(nodeRuntimeData.getType());
    }
}
