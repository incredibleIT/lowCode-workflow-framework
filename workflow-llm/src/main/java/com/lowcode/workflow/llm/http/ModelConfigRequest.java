package com.lowcode.workflow.llm.http;


import lombok.Data;

import java.util.Map;

@Data
public class ModelConfigRequest {

    private String provider;
    private String modelName;
    private String apiKey;
    private String baseUrl;
    private Map<String, Object> parameters;
}
