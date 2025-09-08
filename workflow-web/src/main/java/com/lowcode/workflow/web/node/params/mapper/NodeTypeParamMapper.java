package com.lowcode.workflow.web.node.params.mapper;


import com.lowcode.workflow.web.node.entity.NodeTypeParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 节点类型参数mapper
 */
@Mapper
public interface NodeTypeParamMapper {

    List<NodeTypeParam> findByTypeIds(List<Long> ids);

}
