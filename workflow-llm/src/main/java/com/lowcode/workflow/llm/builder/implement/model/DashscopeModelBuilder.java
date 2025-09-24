package com.lowcode.workflow.llm.builder.implement.model;

import com.lowcode.workflow.llm.annotation.Provider;
import com.lowcode.workflow.llm.builder.better.AbstractModelBuilder;
import com.lowcode.workflow.llm.http.ModelConfigRequest;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Component;


@Component
@Provider("dashscope")
public class DashscopeModelBuilder extends AbstractModelBuilder<QwenChatModel.QwenChatModelBuilder> {
    @Override
    public ChatLanguageModel build(ModelConfigRequest modelConfigRequest) {
        QwenChatModel.QwenChatModelBuilder builder = QwenChatModel.builder();
        // 构建公共的参数
        buildPublicParams(
                builder,
                modelConfigRequest,
                QwenChatModel.QwenChatModelBuilder::temperature,
                QwenChatModel.QwenChatModelBuilder::topP,
                QwenChatModel.QwenChatModelBuilder::maxTokens
        );

        // TODO 构建私有的参数


        builder.apiKey(modelConfigRequest.getApiKey());
        builder.modelName(modelConfigRequest.getModelName());
        return builder.build();
    }


}
