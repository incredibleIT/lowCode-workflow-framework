package com.lowcode.workflow.llm.tools.factory;

import com.google.gson.Gson;
import com.lowcode.workflow.llm.tools.BaseTool;
import com.lowcode.workflow.llm.tools.http.MySqlConnectionInfo;
import com.lowcode.workflow.llm.tools.http.ToolContext;
import com.lowcode.workflow.llm.tools.http.ToolsRequest;
import com.lowcode.workflow.llm.tools.mysql.MysqlTool;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具工厂
 */
@Component
public class ToolFactory {


    private static final Map<String, Class<? extends BaseTool>> TOOL_MAP = new HashMap<>();
    static {
        TOOL_MAP.put("mysql", MysqlTool.class);
    }
    private Gson gson = new Gson();


    public List<Object> createTools(ToolsRequest toolsRequest) {
        // 解析工具的参数
        ToolContext toolContext = parseToolParams(toolsRequest.getToolParams());
        List<Object> tools = new ArrayList<>();
        // 反射去构建工具
        toolsRequest.getEnabledTools().forEach(toolName -> {
            Class<? extends BaseTool> toolClazz = TOOL_MAP.get(toolName);
            if (toolClazz == null) {
                throw new IllegalArgumentException("不支持的工具: " + toolName);
            }

            try {
                Constructor<? extends BaseTool> constructor = toolClazz.getConstructor(ToolContext.class);
                BaseTool tool = constructor.newInstance(toolContext);
                tools.add(tool);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



        return tools;
    }


    /**
     * 构建工具上下文
     * @param toolParams 工具参数, 例如: { "mysql": { "host": "...", "port": 3306, ... } }
     * @return 工具上下文
     */
    private ToolContext parseToolParams(Map<String, Object> toolParams) {
        ToolContext toolContext = new ToolContext();

        if (toolParams.containsKey("mysql")) {
            Map<String, Object> mysqlParams = (Map<String, Object>) toolParams.get("mysql");

            String mysqlJson = gson.toJson(mysqlParams);
            MySqlConnectionInfo mySqlConnectionInfo = gson.fromJson(mysqlJson, MySqlConnectionInfo.class);
            toolContext.setMySqlConnectionInfo(mySqlConnectionInfo);
        }

        return toolContext;
    }





}
