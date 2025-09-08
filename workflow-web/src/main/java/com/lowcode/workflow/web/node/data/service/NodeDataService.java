package com.lowcode.workflow.web.node.data.service;


import com.lowcode.workflow.web.node.data.mapper.NodeDataMapper;
import com.lowcode.workflow.web.node.entity.NodeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeDataService {

    @Autowired
    private NodeDataMapper nodeDataMapper;

    public List<NodeData> getNodeData(String flowId, String version) {

        return nodeDataMapper.findNodeData(flowId, version);
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
