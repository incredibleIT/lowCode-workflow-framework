package com.lowcode.workflow.web.http;

import com.lowcode.workflow.web.flow.entity.Flow;
import lombok.Data;


@Data
public class FlowRequest extends PageRequest {
    private String name;
    private String username;
    private Flow.Status status;
}
