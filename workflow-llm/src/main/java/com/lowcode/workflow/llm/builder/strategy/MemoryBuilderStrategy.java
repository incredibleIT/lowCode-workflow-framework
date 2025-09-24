package com.lowcode.workflow.llm.builder.strategy;


import com.lowcode.workflow.llm.http.MemoryConfigRequest;
import dev.langchain4j.memory.ChatMemory;

/**
 * 记忆构建策略
 */
public interface MemoryBuilderStrategy {

    ChatMemory build(MemoryConfigRequest memoryConfigRequest);
}
