package com.lowcode.workflow.llm.builder.better;

import com.lowcode.workflow.llm.builder.strategy.MemoryBuilderStrategy;
import com.lowcode.workflow.llm.http.MemoryConfigRequest;

/**
 * 增强接口
 */
public abstract class AbstractMemoryBuilder<T> implements MemoryBuilderStrategy {


    protected void buildPublicParams(T builder, MemoryConfigRequest memoryConfigRequest) {

    }

}
