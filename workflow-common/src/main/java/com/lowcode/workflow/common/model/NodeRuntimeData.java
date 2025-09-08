package com.lowcode.workflow.common.model;


import lombok.Data;
import org.bson.Document;

/**
 * 节点运行时数据结构
 */
@Data
public class NodeRuntimeData {
    private String id;
    /** 流id */
    private String flowId;
    /** 节点名称 */
    private String nodeName;
    /** 节点类型 */
    private String type;
    /** 节点属性参数 */
    private Document params;
    /** 连线起始点id */
    private String from;
    /** 连线结束点id */
    private String to;
}
