package com.lowcode.workflow.runner.node.rabbit.entity;


import lombok.Data;

import java.util.Map;

@Data
public class RabbitBinding {

    private String source;
    private String vhost;
    private String destination;
    private String destination_type;
    private String routing_key;
    private Map<String, Object> arguments;
    private String properties_key;
}
