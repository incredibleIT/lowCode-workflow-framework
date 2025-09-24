package com.lowcode.workflow.llm.factory;


import com.lowcode.workflow.llm.builder.strategy.MemoryBuilderStrategy;
import com.lowcode.workflow.llm.http.MemoryConfigRequest;
import dev.langchain4j.memory.ChatMemory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemoryBuilderFactory {

    private Map<String, MemoryBuilderStrategy> memoryBuilderMap;

    public MemoryBuilderFactory(List<MemoryBuilderStrategy> builders) {

        this.memoryBuilderMap = builders.stream().collect(Collectors.toMap(
                b -> b.getClass().getSimpleName().replace("Builder", "").toLowerCase(), b -> b
        ));
    }


    public ChatMemory build(MemoryConfigRequest memoryConfigRequest) {

        MemoryBuilderStrategy builder = memoryBuilderMap.get(memoryConfigRequest.getMemoryType());

        return builder.build(memoryConfigRequest);
    }
}
