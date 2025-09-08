package com.lowcode.workflow.runner.enu;

import com.lowcode.workflow.runner.node.Node;
import com.lowcode.workflow.runner.node.start.StartNode;
import lombok.Getter;


@Getter
public enum NodeTypeEnum {

    /** 基础节点 */
    START("start", StartNode.class);

    private final String type;
    private final Class<? extends Node> clazz;



    NodeTypeEnum(String type, Class<? extends Node> clazz) {
        this.type = type;
        this.clazz = clazz;
    }


}
