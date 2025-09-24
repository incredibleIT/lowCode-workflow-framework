package com.lowcode.workflow.llm.builder;


import com.lowcode.workflow.llm.factory.MemoryBuilderFactory;
import com.lowcode.workflow.llm.factory.ModelBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgentBuilder {

    @Autowired
    private ModelBuilderFactory modelBuilderFactory;

    @Autowired
    private MemoryBuilderFactory memoryBuilderFactory;












}
