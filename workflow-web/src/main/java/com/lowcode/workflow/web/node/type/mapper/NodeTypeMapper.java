package com.lowcode.workflow.web.node.type.mapper;


import com.lowcode.workflow.web.node.entity.NodeType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeTypeMapper {
    List<NodeType> findNodeTypes(String name);

    List<NodeType> findByIds(List<Long> ids);
}
