package com.lowcode.workflow.llm.factory;


import com.lowcode.workflow.llm.annotation.Provider;
import com.lowcode.workflow.llm.builder.strategy.ModelBuilderStrategy;
import com.lowcode.workflow.llm.http.ModelConfigRequest;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 引入工厂模式
 */
@Component
public class ModelBuilderFactory {

    private final Map<String, ModelBuilderStrategy> buildersMap;

    public ModelBuilderFactory(List<ModelBuilderStrategy> builders) {
        // 读取所有的，模型构造器
        this.buildersMap = builders.stream().collect(Collectors.toMap(
                this::getProvider, b -> b
        ));
    }

    /**
     * 获取Provider注解的值
     * @param builder  模型构造器
     * @return provider值
     */
    private String getProvider(ModelBuilderStrategy builder) {
        Provider providerAnnotation = builder.getClass().getAnnotation(Provider.class);
        if (providerAnnotation == null) {
            System.out.println("当前的模型构造器没有Provider注解, 请检查配置");
            return null;
        }
        return providerAnnotation.value();
    }


    public ChatLanguageModel build (ModelConfigRequest modelConfigRequest) {
        String provider = modelConfigRequest.getProvider();
        buildersMap.forEach((k, v) -> System.out.println(k + " " + v));
        ModelBuilderStrategy builder = buildersMap.get(provider);
        if (builder == null) {
            // TODO 抛出配置模型不合法的错误
            System.out.println("模型构造器不存在, 请检查配置");
            return null;
        }
        return builder.build(modelConfigRequest);

    }
}
