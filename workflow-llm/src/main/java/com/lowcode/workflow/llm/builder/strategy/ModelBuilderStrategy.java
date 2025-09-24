package com.lowcode.workflow.llm.builder.strategy;

import com.lowcode.workflow.llm.http.ModelConfigRequest;
import dev.langchain4j.model.chat.ChatLanguageModel;


/**
 * 模型构建策略
 */
public interface ModelBuilderStrategy {
    ChatLanguageModel build(ModelConfigRequest modelConfigRequest);
}
