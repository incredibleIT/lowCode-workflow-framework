package com.lowcode.workflow.llm.tools.http;


import com.google.gson.Gson;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ToolsRequest {

    /**
     * 启用的工具列表
     */
    private List<String> enabledTools;


    /**
     * 工具参数, 例如: { "mysql": { "host": "...", "port": 3306, ... } }
     */
    private Map<String, Object> toolParams;

}
