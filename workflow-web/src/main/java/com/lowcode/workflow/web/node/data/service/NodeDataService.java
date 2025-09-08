package com.lowcode.workflow.web.node.data.service;


import com.lowcode.workflow.common.utils.MergeCollectionsBuilder;
import com.lowcode.workflow.web.node.data.mapper.NodeDataMapper;
import com.lowcode.workflow.web.node.entity.NodeData;
import com.lowcode.workflow.web.node.entity.NodeType;
import com.lowcode.workflow.web.node.type.mapper.NodeTypeMapper;
import com.lowcode.workflow.web.node.type.service.NodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeDataService {

    @Autowired
    private NodeDataMapper nodeDataMapper;

    @Autowired
    private NodeTypeService nodeTypeService;

    public List<NodeData> getNodeData(String flowId,@Nullable String version) {

        List<NodeData> nodeDataList = nodeDataMapper.findNodeData(flowId, version);


        return MergeCollectionsBuilder.source(nodeDataList, NodeData::getTypeId)
                .target(nodeTypeService::findByIds, NodeType::getId)
                .mergeTtoS(NodeData::setNodeType);
    }

    public void updateNodeData(List<NodeData> nodeData) {

        String flowId = nodeData.get(0).getFlowId();

        nodeDataMapper.deleteNodeDatas(flowId);

        nodeDataMapper.saveNodeDatas(nodeData);

    }

    public List<NodeData> getNodeDataVersion(String flowId) {

        return nodeDataMapper.findNodeDataVersion(flowId);
    }

    public void setNodeDataVersion(List<NodeData> nodeDataList) {

        nodeDataMapper.saveNodeDatas(nodeDataList);
    }
}
