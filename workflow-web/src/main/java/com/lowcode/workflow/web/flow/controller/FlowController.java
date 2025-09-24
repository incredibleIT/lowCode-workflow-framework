package com.lowcode.workflow.web.flow.controller;


import com.lowcode.workflow.common.http.Result;
import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.web.flow.entity.Flow;
import com.lowcode.workflow.web.flow.service.FlowService;
import com.lowcode.workflow.web.http.FlowRequest;
import com.lowcode.workflow.web.http.PageResult;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 流程控制器
 */
@RestController
@RequestMapping("/api")
public class FlowController {


    @Autowired
    private FlowService flowService;

    @GetMapping("/flow")
    public PageResult<Flow> listFlow(FlowRequest flowRequest) {
        // TODO 获取登录的用户
//        String name = "kk";
//        flowRequest.setUsername("admin".equals(name) ? null : name);
        return PageResult.of(flowService.listFlowByPage(flowRequest));
    }


    @PostMapping("/flow")
    public Result<Flow> saveFlow(@RequestBody Flow flow) {
        // TODO 设置当前登录的用户
        flowService.saveFlow(flow);

        return Result.of(flow);
    }

    @PutMapping("/flow")
    public Result<Flow> updateFlow(@RequestBody Flow flow) {
        return Result.of(flowService.updateFlow(flow));
    }

    @DeleteMapping("/flow")
    public Result<String> deleteFlow(@RequestBody List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            flowService.deleteFlow(ids);
        }
        return Result.ok();
    }


}
