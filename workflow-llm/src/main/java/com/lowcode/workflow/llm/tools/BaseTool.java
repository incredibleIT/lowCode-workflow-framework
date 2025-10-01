package com.lowcode.workflow.llm.tools;

import com.lowcode.workflow.llm.tools.http.ToolContext;

/**
 * 工具基类
 */
public abstract class BaseTool {

    protected ToolContext toolContext;

    public BaseTool(ToolContext toolContext) {
        this.toolContext = toolContext;
    }

}
