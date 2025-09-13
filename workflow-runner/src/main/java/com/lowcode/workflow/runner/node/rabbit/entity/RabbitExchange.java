package com.lowcode.workflow.runner.node.rabbit.entity;


import lombok.Data;

import java.util.Map;

@Data
public class RabbitExchange {

    private String name;
    private String vhost;
    private String type;
    private boolean durable;
    private boolean auto_delete;
    private boolean internal;
    private Map<String, Object> arguments;
    private MessageStats message_stats;
    private String user_who_performed_action;


    @Data
    public static class MessageStats {
        private long publish_in;
        private RateDetails publish_in_details;
        private long publish_out;
        private RateDetails publish_out_details;
    }

    @Data
    public static class RateDetails {
        private double rate;
    }
}
