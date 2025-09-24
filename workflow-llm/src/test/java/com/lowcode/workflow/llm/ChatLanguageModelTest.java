package com.lowcode.workflow.llm;


import com.lowcode.workflow.llm.builder.implement.model.DashscopeModelBuilder;
import com.lowcode.workflow.llm.factory.MemoryBuilderFactory;
import com.lowcode.workflow.llm.factory.ModelBuilderFactory;
import com.lowcode.workflow.llm.http.MemoryConfigRequest;
import com.lowcode.workflow.llm.http.MemoryStoreConfigRequest;
import com.lowcode.workflow.llm.http.ModelConfigRequest;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatLanguageModelTest {

    @Autowired
    private ModelBuilderFactory modelBuilderFactory;

    @Autowired
    private MemoryBuilderFactory memoryBuilderFactory;

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



}
