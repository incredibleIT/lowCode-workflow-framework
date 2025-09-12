package com.lowcode.workflow.runner.node;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

/**
 * 节点抽象
 */
public abstract class DefaultNode implements Node {
    // 上一个节点的输入参数
    @Getter
    private Document input;

    // 本节点的自定义参数
    private Document payloadParams;

    // 当前运行状态
    @Setter
    private Status status = Status.RUNNING;



    // 当前节点所属流程Id和节点Id
    @Getter
    private String flowId;

    @Getter
    private String nodeId;


    /**
     * 构造当前节点参数
     * @param params 参数
     */
    public DefaultNode(Document params) {
        if (params == null) {
            throw new IllegalArgumentException("params is null");
        }
        this.flowId = params.getString("flowId");
        this.nodeId = params.getString("nodeId");
        params.remove("flowId");
        params.remove("nodeId");


        this.input = params.get("input", Document.class);
        this.payloadParams = params.get("payload", Document.class);
        // 校验校验并赋值参数
        this.verify(params);
    }


    public Document putToNextNodeInput() {

        // TODO 判空
        return new Document("input", this.payloadParams);
    }


    @Override
    public Status getStatus() {
        return this.status;
    }

    // 校验节点参数
    protected abstract void verify(Document params);

}
