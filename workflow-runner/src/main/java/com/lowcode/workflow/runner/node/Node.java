package com.lowcode.workflow.runner.node;


/**
 * 节点接口, 规范所有节点的行为
 */
public interface Node {

    /**
     * 运行当前节点
     * @param callback 回调函数
     */
    void run(CallbackFunction callback);

    /**
     * 获取节点状态
     * @return 节点状态
     */
    Status getStatus();


    /**
     * 节点状态
     */
    enum Status {
        /**
         * 节点运行中
         */
        RUNNING,
        /**
         * 节点运行完成
         */
        FINISHED,
        /**
         * 节点运行失败
         */
        FAILED
    }
}
