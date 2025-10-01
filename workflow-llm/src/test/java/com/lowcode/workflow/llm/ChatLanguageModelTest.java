package com.lowcode.workflow.llm;


import com.lowcode.workflow.llm.builder.AgentBuilder;
import com.lowcode.workflow.llm.builder.implement.model.DashscopeModelBuilder;
import com.lowcode.workflow.llm.factory.MemoryBuilderFactory;
import com.lowcode.workflow.llm.factory.ModelBuilderFactory;
import com.lowcode.workflow.llm.http.MemoryConfigRequest;
import com.lowcode.workflow.llm.http.MemoryStoreConfigRequest;
import com.lowcode.workflow.llm.http.ModelConfigRequest;
import com.lowcode.workflow.llm.tools.factory.ToolFactory;
import com.lowcode.workflow.llm.tools.http.ToolsRequest;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ChatLanguageModelTest {

    @Autowired
    private ModelBuilderFactory modelBuilderFactory;

    @Autowired
    private MemoryBuilderFactory memoryBuilderFactory;

    @Autowired
    private AgentBuilder agentBuilder;

    @Autowired
    private ToolFactory toolFactory;

    @Test
    public void test01() {
        ModelConfigRequest modelConfigRequest = new ModelConfigRequest();
        modelConfigRequest.setModelName("qwen-plus");
        modelConfigRequest.setApiKey("sk-d7d1bd06e3874b57a1fb4668510a7c96");
        modelConfigRequest.setProvider("tongyi");

        if (modelConfigRequest.getProvider().equals("tongyi")) {
            ChatLanguageModel chatModel = new DashscopeModelBuilder().build(modelConfigRequest);
            System.out.println(chatModel.chat("你好, 做个自我介绍吧"));
        }
    }



    @Test
    public void test02() {
        ModelConfigRequest modelConfigRequest = new ModelConfigRequest();
        modelConfigRequest.setModelName("qwen-plus");
        modelConfigRequest.setApiKey("sk-d7d1bd06e3874b57a1fb4668510a7c96");

        // dashscope
        modelConfigRequest.setProvider("dashscope");

        ChatLanguageModel chatLanguageModel = modelBuilderFactory.build(modelConfigRequest);

        System.out.println(chatLanguageModel.chat("做个自我介绍吧"));
    }


    @Test
    public void test03() {
        ModelConfigRequest modelConfigRequest = new ModelConfigRequest();
        modelConfigRequest.setModelName("qwen-plus");
        modelConfigRequest.setApiKey("sk-d7d1bd06e3874b57a1fb4668510a7c96");
        modelConfigRequest.setProvider("dashscope");
        ChatLanguageModel chatLanguageModel = modelBuilderFactory.build(modelConfigRequest);
        System.out.println(chatLanguageModel.chat(UserMessage.from("做个自我介绍吧")));
    }


    @Test
    public void test04() {
        ModelConfigRequest modelConfigRequest = new ModelConfigRequest();
        modelConfigRequest.setModelName("qwen-plus");
        modelConfigRequest.setApiKey("sk-d7d1bd06e3874b57a1fb4668510a7c96");
        modelConfigRequest.setProvider("dashscope");

        MemoryConfigRequest memoryConfigRequest = new MemoryConfigRequest();
        memoryConfigRequest.setId("000000000001");
        memoryConfigRequest.setMaxMessages(2);
        memoryConfigRequest.setMemoryType("simplememorymessage");
        MemoryStoreConfigRequest memoryStoreConfigRequest = new MemoryStoreConfigRequest();
        memoryStoreConfigRequest.setStoreType("inmemorystore");

        memoryConfigRequest.setMemoryStoreConfig(memoryStoreConfigRequest);

        ChatLanguageModel chatLanguageModel = modelBuilderFactory.build(modelConfigRequest);
        ChatMemory chatMemory = memoryBuilderFactory.build(memoryConfigRequest);
    }



    @Test
    public void test05() {
//        UserDefinedChat userDefinedChat = agentBuilder.buildAgent();

//        System.out.println(userDefinedChat.chat("为我查询数据库中eda_flow表的所有数据, 并返回结果"));
    }



    @Test
    public void test06() {
        ModelConfigRequest modelConfigRequest = new ModelConfigRequest();
        modelConfigRequest.setApiKey("sk-d7d1bd06e3874b57a1fb4668510a7c96");
        modelConfigRequest.setModelName("qwen-plus");
        modelConfigRequest.setProvider("dashscope");

        ToolsRequest toolsRequest = new ToolsRequest();
        ArrayList<String> arr = new ArrayList<>();
        arr.add("mysql");
        toolsRequest.setEnabledTools(arr);
        Map<String, Object> toolParams = new HashMap<>();
        Map<String, Object> v1 = new HashMap<>();
        v1.put("host", "127.0.0.1");
        v1.put("port", 3306);
        v1.put("database", "flow_eda");
        v1.put("username", "root");
        v1.put("password", "yy3908533");
        toolParams.put("mysql", v1);
        toolsRequest.setToolParams(toolParams);
        MemoryConfigRequest memoryConfigRequest = new MemoryConfigRequest();
        memoryConfigRequest.setMemoryType("simplememorymessage");
        memoryConfigRequest.setMaxMessages(20);
        memoryConfigRequest.setId("000000000001");
        MemoryStoreConfigRequest memoryStoreConfigRequest = new MemoryStoreConfigRequest();
        memoryStoreConfigRequest.setStoreType("inmemorystore");
        memoryConfigRequest.setMemoryStoreConfig(memoryStoreConfigRequest);

        System.out.println(modelConfigRequest.toString());
        System.out.println(memoryConfigRequest.toString());
        System.out.println(toolsRequest.toString());


        UserDefinedChat chat = agentBuilder.buildAgent(
                modelConfigRequest,
                memoryConfigRequest,
                toolsRequest
        );

        System.out.println(chat.chat("为我查询数据库中eda_flow表的所有数据, 并返回结果"));


    }
}
