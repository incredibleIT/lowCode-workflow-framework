package com.lowcode.workflow.llm.tools.http;

import lombok.Getter;
import lombok.Setter;

public class ToolContext {

    /**
     * 工具上下文
     */
    @Getter
    @Setter
    private MySqlConnectionInfo mySqlConnectionInfo;
}
