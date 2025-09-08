package com.lowcode.workflow.web.node.data.controller;


import com.lowcode.workflow.common.http.Result;
import com.lowcode.workflow.web.node.data.service.NodeDataService;
import com.lowcode.workflow.web.node.entity.NodeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api")
public class NodeDataController {

    @Autowired
    private NodeDataService nodeDataService;



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


    public Result<String> runNodeData(@RequestParam String flowId) {
        return null;
    }


    public Result<String> stopNodeData(@RequestParam String flowId) {
        return null;
    }
}
