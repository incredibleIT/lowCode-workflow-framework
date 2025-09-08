package com.lowcode.workflow.web.node.type.controller;


import com.lowcode.workflow.common.http.Result;
import com.lowcode.workflow.web.node.entity.NodeType;
import com.lowcode.workflow.web.node.type.service.NodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class NodeTypeController {


    @Autowired
    private NodeTypeService nodeTypeService;


    @GetMapping("/nodetype")
    public Result<Map<String, List<NodeType>>> getNodeTypeList(@RequestParam(required = false) String name) {
        return Result.of(nodeTypeService.getNodeTypeList(name));
    }












}
