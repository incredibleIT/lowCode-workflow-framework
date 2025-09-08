package com.lowcode.workflow.web.node.data.mapper;


import com.lowcode.workflow.web.node.entity.NodeData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.lang.Nullable;

import java.util.List;

@Mapper
public interface NodeDataMapper {
    public List<NodeData> findNodeData(String flowId, @Nullable String version);

    public void deleteNodeDatas(String flowId);

    public void saveNodeDatas(List<NodeData> nodeData);

    public List<NodeData> findNodeDataVersion(String flowId);
}
