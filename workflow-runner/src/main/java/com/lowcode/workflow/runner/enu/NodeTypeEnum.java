package com.lowcode.workflow.runner.enu;

import com.lowcode.workflow.common.model.NodeRuntimeData;
import com.lowcode.workflow.runner.node.Node;
import com.lowcode.workflow.runner.node.db.mysql.MysqlNode;
import com.lowcode.workflow.runner.node.output.OutputNode;
import com.lowcode.workflow.runner.node.start.StartNode;
import com.lowcode.workflow.runner.node.timer.TimerNode;
import jdk.nashorn.internal.runtime.regexp.joni.constants.internal.NodeType;
import lombok.Getter;


@Getter
public enum NodeTypeEnum {

    /** 基础节点 */
    START("start", StartNode.class),
    TIMER("timer", TimerNode.class),
    OUTPUT("output", OutputNode.class),
    MYSQL("mysql", MysqlNode.class);


    private final String type;
    private final Class<? extends Node> clazz;



    NodeTypeEnum(String type, Class<? extends Node> clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    private static NodeTypeEnum getNodeTypeEnum(String nodeType) {
        for (NodeTypeEnum value : NodeTypeEnum.values()) {
            if (value.getType().equals(nodeType)) {
                return value;
            }
        }
        return null;
    }


    public static Class<? extends Node> getClazzBynodeRuntimeData(NodeRuntimeData nodeRuntimeData) {
        String nodeType = nodeRuntimeData.getType();
        NodeTypeEnum nodeTypeEnum = getNodeTypeEnum(nodeType);
        if (nodeTypeEnum == null) {
            System.out.println("-----------------(测试)非法的节点类型------------------");
            throw new IllegalArgumentException("-----------------(测试)非法的节点类型------------------");
        }

        return nodeTypeEnum.getClazz();
    }
}
