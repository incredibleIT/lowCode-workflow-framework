package com.lowcode.workflow.web.node.entity;

import lombok.Data;

import java.util.List;


/**
 * 节点类型Bean
 */
@Data
public class NodeType {
    private Long id;
    /** 节点类型 */
    private String type;
    /** 节点类型名称 */
    private String typeName;
    /** 菜单，用于分栏 */
    private String menu;
    /** 节点图标 */
    private String svg;
    /** 节点背景色 */
    private String background;
    /** 节点类型描述 */
    private String description;
    /** 节点参数 */
    private List<NodeTypeParam> params;
}
