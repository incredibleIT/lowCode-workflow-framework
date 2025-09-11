package com.lowcode.workflow.runner.node.start;


import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import org.bson.Document;

/**
 * 开始节点
 */
public class StartNode extends DefaultNode {
    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public StartNode(Document params) {
        super(params);
    }

    @Override
    protected void verify(Document params) {
    }

    @Override
    public void run(CallbackFunction callback) {
        // 设置当前节点运行成功状态
        setStatus(Status.FINISHED);
        // 回调
        callback.callback(putToNextNodeInput());
    }
}
