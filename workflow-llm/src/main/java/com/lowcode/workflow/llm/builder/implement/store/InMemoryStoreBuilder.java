package com.lowcode.workflow.llm.builder.implement.store;

import com.lowcode.workflow.llm.builder.better.AbstractMemoryStoreBuilder;
import com.lowcode.workflow.llm.http.MemoryStoreConfigRequest;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.stereotype.Component;


@Component
public class InMemoryStoreBuilder extends AbstractMemoryStoreBuilder {
    @Override
    public ChatMemoryStore build(MemoryStoreConfigRequest memoryStoreConfigRequest) {
        return new InMemoryChatMemoryStore();
    }
}
