package com.lowcode.workflow.web.node.type.service;


import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.web.node.entity.NodeType;
import com.lowcode.workflow.web.node.entity.NodeTypeParam;
import com.lowcode.workflow.web.node.params.mapper.NodeTypeParamMapper;
import com.lowcode.workflow.web.node.type.mapper.NodeTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NodeTypeService {

    @Autowired
    private NodeTypeMapper nodeTypeMapper;

    @Autowired
    private NodeTypeParamMapper nodeTypeParamMapper;


    private static final List<String> TYPE_MENU =
            Arrays.asList("基础", "运算", "解析", "网络", "数据库", "子流程");

    public Map<String, List<NodeType>> getNodeTypeList(String name) {
        Map<String, List<NodeType>> map = new LinkedHashMap<>();

        List<NodeType> nodeTypeList = nodeTypeMapper.findNodeTypes(name);


        mergeNodeTypeParam(nodeTypeList);

        // 分类
        TYPE_MENU.forEach(menu -> map.put(menu, CollectionUtil.filter(nodeTypeList, nodeType -> nodeType.getMenu().equals(menu))));

        return map;
    }

    public List<NodeType> findByIds(List<Long> ids) {
        return nodeTypeMapper.findByIds(ids);
    }


    private List<NodeType> mergeNodeTypeParam(List<NodeType> nodeTypeList) {

        if (CollectionUtil.isEmpty(nodeTypeList)) {
            return nodeTypeList;
        }

        List<Long> ids = CollectionUtil.map(nodeTypeList, NodeType::getId);

        List<NodeTypeParam> nodeTypeParamList = nodeTypeParamMapper.findByTypeIds(ids);
        System.out.println(nodeTypeParamList);

        nodeTypeList.forEach(nodeType -> nodeType.setParams(CollectionUtil.filter(nodeTypeParamList, nodeTypeParam -> nodeType.getId().equals(nodeTypeParam.getTypeId()))));

        return nodeTypeList;
    }
}
