package com.lowcode.workflow.web.flow.service;


import com.github.pagehelper.PageHelper;
import com.lowcode.workflow.web.flow.entity.Flow;
import com.lowcode.workflow.web.flow.mapper.FlowMapper;
import com.lowcode.workflow.web.http.FlowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 流程服务
 */
@Service
public class FlowService {

    @Autowired
    private FlowMapper flowMapper;


    public List<Flow> listFlowByPage(FlowRequest flowRequest) {
        PageHelper.startPage(flowRequest.getPage(), flowRequest.getLimit());

        return flowMapper.findByRequest(flowRequest);
    }

    public void saveFlow(Flow flow) {

        flow.setStatus(Flow.Status.INIT);
        flow.setCreateDate(new Date());
        flow.setUpdateDate(new Date());

        flowMapper.insert(flow);

    }

    public Flow updateFlow(Flow flow) {

        flow.setUpdateDate(new Date());

        flowMapper.update(flow);


        return findById(flow.getId());
    }


    public Flow findById(String id) {
        return flowMapper.findById(id);
    }


    public void deleteFlow(List<String> ids) {
        flowMapper.deleteByIds(ids);

        // TODO 这个流程中的NodeData也需要被删除
    }


}
