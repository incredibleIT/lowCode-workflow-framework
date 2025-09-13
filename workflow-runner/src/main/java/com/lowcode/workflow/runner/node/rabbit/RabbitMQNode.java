package com.lowcode.workflow.runner.node.rabbit;

import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import org.bson.Document;

public class RabbitMQNode extends DefaultNode {



    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public RabbitMQNode(Document params) {
        super(params);
    }

    @Override
    protected void verify(Document params) {

    }

    @Override
    public void run(CallbackFunction callback) {

    }
}
