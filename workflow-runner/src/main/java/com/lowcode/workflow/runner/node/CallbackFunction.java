package com.lowcode.workflow.runner.node;


import org.bson.Document;


/**
 * 回调函数接口
 */
@FunctionalInterface
public interface CallbackFunction {

    /**
     * 回调函数
     * @param params 回调参数
     */
    void callback(Document params);
}
