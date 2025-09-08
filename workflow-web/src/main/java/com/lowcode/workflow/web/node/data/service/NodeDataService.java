package com.lowcode.workflow.web.node.data.service;


import com.lowcode.workflow.common.api.NodeRuntimeDataService;
import com.lowcode.workflow.common.model.NodeRuntimeData;
import com.lowcode.workflow.common.utils.MergeCollectionsBuilder;
import com.lowcode.workflow.web.node.data.mapper.NodeDataMapper;
import com.lowcode.workflow.web.node.entity.NodeData;
import com.lowcode.workflow.web.node.entity.NodeType;
import com.lowcode.workflow.web.node.type.mapper.NodeTypeMapper;
import com.lowcode.workflow.web.node.type.service.NodeTypeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class NodeDataService {

    @Autowired
    private NodeDataMapper nodeDataMapper;

    @Autowired
    private NodeTypeService nodeTypeService;

    @DubboReference
    private NodeRuntimeDataService nodeRuntimeDataService;

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

    /**
     * 在运行时, 本流程的实例节点数据会被系统转换为节点运行时数据NodeRuntimeData
     * @param flowId 要执行的流程
     */
    public void runNodeData(String flowId) {
        System.out.println("--------test------------即将推送到运行时引擎");
        // 将运行时数据提交到运行时引擎
        nodeRuntimeDataService.runNodeRuntimeData(getAndConvertNodeDataListToNodeRuntimeDataList(flowId));
    }

    private List<NodeRuntimeData> getAndConvertNodeDataListToNodeRuntimeDataList(String flowId) {
        // 本流程的实例节点数据
        List<NodeData> nodeDataList = getNodeData(flowId, null);
        // 需要转换为的运行时节点数据
        List<NodeRuntimeData> nodeRuntimeDataList = new ArrayList<>();
        nodeDataList.forEach(nodeData -> nodeRuntimeDataList.add(convert(nodeData)));
        return nodeRuntimeDataList;
    }



    private NodeRuntimeData convert(NodeData nodeData) {
        NodeRuntimeData nodeRuntimeData = new NodeRuntimeData();
        nodeRuntimeData.setId(nodeData.getId());
        nodeRuntimeData.setFlowId(nodeData.getFlowId());
        nodeRuntimeData.setNodeName(nodeData.getNodeName());
        nodeRuntimeData.setFrom(nodeData.getFrom());
        nodeRuntimeData.setTo(nodeData.getTo());
        if (nodeData.getNodeType() != null) {
            nodeRuntimeData.setType(nodeData.getNodeType().getType());
        }

        Document params = null;

        if (nodeData.getParams() != null) {
            params = nodeData.getParams();
            if (nodeData.getPayload() != null) {
                params.append("payload", nodeData.getPayload());
            }
        } else if (nodeData.getPayload() != null) {
            params = new Document("payload", nodeData.getPayload());
        }

        nodeRuntimeData.setParams(params);
        return nodeRuntimeData;
    }
}
