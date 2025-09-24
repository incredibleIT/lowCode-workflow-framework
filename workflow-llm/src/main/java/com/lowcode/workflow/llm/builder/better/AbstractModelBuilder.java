package com.lowcode.workflow.llm.builder.better;


import com.lowcode.workflow.llm.builder.strategy.ModelBuilderStrategy;
import com.lowcode.workflow.llm.http.ModelConfigRequest;
import com.lowcode.workflow.llm.utils.SafeGetValue;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 一个默认实现, 用来构建模型共有的参数
 */
public abstract class AbstractModelBuilder<T> implements ModelBuilderStrategy {

    /**
     * 构建模型共有的参数
     */
    protected void buildPublicParams(T builder,
                                     ModelConfigRequest modelConfigRequest,
                                     BiConsumer<T, Float> temperatureSetter,
                                     BiConsumer<T, Double> topPSetter,
                                     BiConsumer<T, Integer> maxTokensSetter
                                     /*BiConsumer<T, Integer> maxRetriesSetter,
                                     BiConsumer<T, Duration> timeoutSetter,
                                     BiConsumer<T, Boolean> logRequestsSetter,
                                     BiConsumer<T, Boolean> logResponsesSetter*/) {
        Map<String, Object> params = modelConfigRequest.getParameters();
        if (params != null && !params.isEmpty()) {
            SafeGetValue.extractFloat("temperature", params).ifPresent(value -> temperatureSetter.accept(builder, value));
            SafeGetValue.extractDouble("topP", params).ifPresent(value -> topPSetter.accept(builder, value));
            SafeGetValue.extractInteger("maxTokens", params).ifPresent(value -> maxTokensSetter.accept(builder, value));
        }
    }
}
