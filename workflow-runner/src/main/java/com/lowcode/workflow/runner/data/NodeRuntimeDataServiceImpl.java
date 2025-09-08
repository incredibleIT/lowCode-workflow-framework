package com.lowcode.workflow.runner.data;


import com.lowcode.workflow.common.api.NodeRuntimeDataService;
import com.lowcode.workflow.common.model.NodeRuntimeData;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DubboService(interfaceClass = NodeRuntimeDataService.class)
public class NodeRuntimeDataServiceImpl implements NodeRuntimeDataService{


    /**
     * 负责将运行时节点数据提交到 运行时引擎模块 进行执行
     * @param nodeRuntimeDataList 运行时数据
     */
    @Override
    public void runNodeRuntimeData(List<NodeRuntimeData> nodeRuntimeDataList) {

        System.out.println("由web模块提交来需要运行的节点运行时数据" + nodeRuntimeDataList);

    }
}
