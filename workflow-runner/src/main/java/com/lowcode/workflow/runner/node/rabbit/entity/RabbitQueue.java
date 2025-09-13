package com.lowcode.workflow.runner.node.rabbit.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
public class RabbitQueue {

    // 基本信息
    private String name;
    private String vhost;
    private boolean durable;
    private boolean auto_delete;
    private boolean exclusive;
    private String state;
    private String type;
    private String node;
    private int memory;
    private Map<String, Object> arguments; // 如 x-message-ttl, x-dead-letter-exchange 等

    // 消息数量相关
    private int messages;
    private int messages_ready;
    private int messages_unacknowledged;

    // 消息字节数相关
    private long message_bytes;
    private long message_bytes_ready;
    private long message_bytes_unacknowledged;
    private long message_bytes_ram;

    // 速率详情（嵌套对象）
    private RateInfo messages_details;
    private RateInfo messages_ready_details;
    private RateInfo messages_unacknowledged_details;
    private RateInfo reductions_details;

    // 其他统计
//    private MessageStats message_stats;
    private int consumers;
    private double consumer_utilisation;
    private int reductions;
    private Map<String, Object> effective_policy_definition;



    @Setter
    @Getter
    public static class RateInfo {
        private double rate;

        @Override
        public String toString() {
            return "RateInfo{" + "rate=" + rate + '}';
        }
    }





}
