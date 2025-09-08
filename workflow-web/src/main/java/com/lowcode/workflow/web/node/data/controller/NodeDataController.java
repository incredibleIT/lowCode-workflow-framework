package com.lowcode.workflow.web.node.data.controller;


import com.lowcode.workflow.common.http.Result;
import com.lowcode.workflow.web.node.data.service.NodeDataService;
import com.lowcode.workflow.web.node.entity.NodeData;
import com.lowcode.workflow.web.node.type.service.NodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class NodeDataController {

    @Autowired
    private NodeDataService nodeDataService;

    @Autowired
    private NodeTypeService nodeTypeService;



    @GetMapping("/nodedata")
    public Result<List<NodeData>> getNodeData(@RequestParam("flowId") String flowId, @RequestParam(required = false) String version) {
        return Result.of(nodeDataService.getNodeData(flowId, version));
    }


    @PostMapping("/nodedata")
    public Result<String> setNodeData(@RequestBody List<NodeData> nodeData) {

        nodeDataService.updateNodeData(nodeData);

        return Result.ok();

    }

    @GetMapping("/nodedataversion")
    public Result<List<NodeData>> getNodeDataVersion(@RequestParam("flowId") String flowId) {

        return Result.of(nodeDataService.getNodeDataVersion(flowId));

    }


    @PostMapping("/nodedataversion")
    public Result<String> setNodeDataVersion(@RequestParam("version") String version, @RequestBody List<NodeData> nodeDataList) {
        nodeDataList.forEach(nodeData -> nodeData.setVersion(version));
        nodeDataService.setNodeDataVersion(nodeDataList);
        return Result.ok();
    }


    /**
     * 系统会在运行DataNode时将DataNode实例数据构建为FlowRunTimeData运行时节点数据
     * @param flowId
     * @return
     */
    @PostMapping("/runnode")
    public Result<String> runNodeData(@RequestParam String flowId) {
        nodeDataService.runNodeData(flowId);
        return Result.ok();
    }


    @PostMapping("/stopnode")
    public Result<String> stopNodeData(@RequestParam String flowId) {
        return Result.ok();
    }
}
