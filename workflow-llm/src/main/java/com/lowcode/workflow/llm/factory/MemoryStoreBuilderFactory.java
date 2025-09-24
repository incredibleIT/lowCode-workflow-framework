package com.lowcode.workflow.llm.factory;


import com.lowcode.workflow.llm.builder.strategy.MemoryStoreStrategy;
import com.lowcode.workflow.llm.http.MemoryStoreConfigRequest;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemoryStoreBuilderFactory {
    private final Map<String, MemoryStoreStrategy> builderMap;



    public MemoryStoreBuilderFactory(List<MemoryStoreStrategy> builders) {
        this.builderMap = builders.stream().collect(Collectors.toMap(
                        b -> b.getClass().getSimpleName().replace("Builder", "").toLowerCase(),b -> b
                ));
    }


    public ChatMemoryStore build(MemoryStoreConfigRequest memoryStoreConfigRequest) {
        MemoryStoreStrategy builder = builderMap.get(memoryStoreConfigRequest.getStoreType());

        return builder.build(memoryStoreConfigRequest);
    }



}
