package com.lowcode.workflow.llm.http;


import lombok.Data;

@Data
public class MemoryConfigRequest {

    private String id;

    private Integer maxMessages;

    private MemoryStoreConfigRequest memoryStoreConfig;

    private String memoryType;
}
