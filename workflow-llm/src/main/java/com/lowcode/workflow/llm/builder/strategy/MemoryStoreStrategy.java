package com.lowcode.workflow.llm.builder.strategy;


import com.lowcode.workflow.llm.http.MemoryStoreConfigRequest;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

/**
 * 模型记忆存储介质策略
 */
public interface MemoryStoreStrategy {
    ChatMemoryStore build(MemoryStoreConfigRequest memoryStoreConfigRequest);
}
