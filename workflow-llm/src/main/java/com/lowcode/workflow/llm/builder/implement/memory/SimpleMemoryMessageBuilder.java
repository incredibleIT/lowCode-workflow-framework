package com.lowcode.workflow.llm.builder.implement.memory;


import com.lowcode.workflow.llm.builder.better.AbstractMemoryBuilder;
import com.lowcode.workflow.llm.factory.MemoryStoreBuilderFactory;
import com.lowcode.workflow.llm.http.MemoryConfigRequest;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleMemoryMessageBuilder extends AbstractMemoryBuilder<MessageWindowChatMemory.Builder> {

    @Autowired
    private MemoryStoreBuilderFactory memoryStoreBuilderFactory;

    @Override
    public ChatMemory build(MemoryConfigRequest memoryConfigRequest) {
        MessageWindowChatMemory.Builder builder = MessageWindowChatMemory.builder();
        this.buildPublicParams(builder, memoryConfigRequest);
        builder.id(memoryConfigRequest.getId())
                .maxMessages(memoryConfigRequest.getMaxMessages())
                .chatMemoryStore(memoryStoreBuilderFactory.build(memoryConfigRequest.getMemoryStoreConfig()));  // 我这里可以设计一个MemoryStore的构建工厂, 来通过前端传来的参数来决定使用哪种存储介质我们将他构建出来作为参数

        return builder.build();
    }
}
