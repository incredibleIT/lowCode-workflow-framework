package com.lowcode.workflow.llm.builder;


import com.lowcode.workflow.llm.UserDefinedChat;
import com.lowcode.workflow.llm.factory.MemoryBuilderFactory;
import com.lowcode.workflow.llm.factory.ModelBuilderFactory;
import com.lowcode.workflow.llm.http.MemoryConfigRequest;
import com.lowcode.workflow.llm.http.ModelConfigRequest;
import com.lowcode.workflow.llm.tools.BaseTool;
import com.lowcode.workflow.llm.tools.factory.ToolFactory;
import com.lowcode.workflow.llm.tools.http.MySqlConnectionInfo;
import com.lowcode.workflow.llm.tools.http.ToolsRequest;
import com.lowcode.workflow.llm.tools.mysql.MysqlTool;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.tools.Tool;
import java.util.*;

@Component
public class AgentBuilder {

    @Autowired
    private ModelBuilderFactory modelBuilderFactory;

    @Autowired
    private MemoryBuilderFactory memoryBuilderFactory;

    @Autowired
    private ToolFactory toolFactory;


    /**
     * 构建智能体
     * @param modelConfigRequest 模型配置请求
     * @param memoryConfigRequest 内存配置请求
     * @param toolsRequest 工具配置请求
     * @return 智能体
     */
    public UserDefinedChat buildAgent(ModelConfigRequest modelConfigRequest,
                                      MemoryConfigRequest memoryConfigRequest,
                                      ToolsRequest toolsRequest
                                      ) {
        ChatLanguageModel model = modelBuilderFactory.build(modelConfigRequest);
        ChatMemory chatMemory = memoryBuilderFactory.build(memoryConfigRequest);
        List<Object> tools = toolFactory.createTools(toolsRequest);


        return AiServices.builder(UserDefinedChat.class)
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .tools(tools)
                .build();
    }





}