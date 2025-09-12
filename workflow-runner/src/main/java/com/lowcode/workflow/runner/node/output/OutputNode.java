package com.lowcode.workflow.runner.node.output;


import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

@Slf4j
public class OutputNode extends DefaultNode {

    Document p = new Document();


    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public OutputNode(Document params) {
        super(params);
        p.putAll(params);
        if (this.getInput() != null) {
            p.append("params", this.getInput());
        }
        p.putAll(this.putToNextNodeInput());
    }

    @Override
    protected void verify(Document params) {}

    @Override
    public void run(CallbackFunction callback) {
        log.info("输出节点{}的输出参数: {}", this.getNodeId(),p);

        // 回调
        callback.callback(p);
    }
}
