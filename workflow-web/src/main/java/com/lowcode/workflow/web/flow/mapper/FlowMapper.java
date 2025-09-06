package com.lowcode.workflow.web.flow.mapper;


import com.lowcode.workflow.web.flow.entity.Flow;
import com.lowcode.workflow.web.http.FlowRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlowMapper {


    List<Flow> findByRequest(FlowRequest flowRequest);


    void insert(Flow flow);


    void update(Flow flow);


    Flow findById(String id);


    void deleteByIds(List<String> ids);
}
