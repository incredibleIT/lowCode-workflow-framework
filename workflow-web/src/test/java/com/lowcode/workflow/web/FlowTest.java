package com.lowcode.workflow.web;


import com.lowcode.workflow.web.flow.service.FlowService;
import com.lowcode.workflow.web.http.FlowRequest;
import com.lowcode.workflow.web.node.data.mapper.NodeDataMapper;
import com.lowcode.workflow.web.node.data.service.NodeDataService;
import com.lowcode.workflow.web.node.entity.NodeType;
import com.lowcode.workflow.web.node.params.mapper.NodeTypeParamMapper;
import com.lowcode.workflow.web.node.type.service.NodeTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class FlowTest {

    @Autowired
    private FlowService flowService;

    @Autowired
    private NodeTypeParamMapper nodeTypeParamMapper;

    @Autowired
    private NodeTypeService nodeTypeService;

    @Autowired
    private NodeDataService nodeDataService;




    @Test
    public void test01() {
        nodeTypeParamMapper.findByTypeIds(Arrays.asList(1L, 2L)).forEach(System.out::println);
        flowService.listFlowByPage(new FlowRequest()).forEach(System.out::println);
    }


    @Test
    public void test02() {
        Map<String, List<NodeType>> nodeTypeList = nodeTypeService.getNodeTypeList(null);

        nodeTypeList.entrySet().forEach(System.out::println);
    }


    @Test
    public void test03() {
        nodeDataService.getNodeData("2i0e08w5ktw000", null).forEach(System.out::println);

//        nodeDataMapper.findNodeData("2i0e08w5ktw000", null).forEach(System.out::println);
    }


    @Test
    public void test04() {
        nodeDataService.runNodeData("2i0e08w5ktw000");
    }
}
