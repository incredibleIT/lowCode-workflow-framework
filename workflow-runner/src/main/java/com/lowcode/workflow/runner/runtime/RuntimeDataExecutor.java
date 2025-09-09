package com.lowcode.workflow.runner.runtime;


import com.lowcode.workflow.common.model.NodeRuntimeData;
import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.enu.NodeTypeEnum;
import com.lowcode.workflow.runner.node.Node;
import org.bson.Document;

import java.awt.print.Pageable;
import java.util.List;

/**
 * 单个流程的执行器
 */
public class RuntimeDataExecutor {

    private final List<NodeRuntimeData> nodeRuntimeDataList;

    private final String flowId;

    public RuntimeDataExecutor(List<NodeRuntimeData> nodeRuntimeDataList) {
        this.nodeRuntimeDataList = nodeRuntimeDataList;
        this.flowId = nodeRuntimeDataList.get(0).getFlowId();
    }

    /**
     * 作为起点开始执行流程
     * @param node 开始节点
     */
    public void start(NodeRuntimeData node) {

        this.run(node);

    }

    /**
     * TODO 执行节点
     * @param currentNode 节点运行时数据
     */
    private void run(NodeRuntimeData currentNode) {
        System.out.println(Thread.currentThread().getName() + "-(测试)-当前正在运行中的节点" + currentNode);
        // 反射获取节点实例instance
        Node nodeInstance = getNodeInstance(currentNode);

        // 运行实例, 并设置回调函数形成调用链路
        nodeInstance.run(param -> {
            // 回调会在节点执行末尾自动执行
            runNextNode(currentNode, param);
        });
    }


    /**
     * 找到并执行下一个节点
     * @param nodeRuntimeData 节点运行时数据
     * @param param 传递的参数
     */
    private void runNextNode(NodeRuntimeData nodeRuntimeData, Document param) {

        // 找到下一个节点
        List<NodeRuntimeData> nextNodes = findNextNode(nodeRuntimeData);
        System.out.println(nodeRuntimeData.getId() + "-(测试)-" + Thread.currentThread().getName() + "的下一个节点有: " + nextNodes);
        // 异步执行节点链, 涉及到一个参数传递的问题
        CollectionUtil.foreach(nextNodes, node -> RuntimeDataThreadPool.getThreadPool(flowId).execute(() -> this.run(setNextNodeInputParam(node, param))));

    }

    private NodeRuntimeData setNextNodeInputParam(NodeRuntimeData nextNode, Document input) {
        Document param = null;
        if (input != null) {
            param = input;
        } else {
            param = new Document();
        }
        nextNode.setParams(param);
        return nextNode;
    }

    private List<NodeRuntimeData> findNextNode(NodeRuntimeData nodeRuntimeData) {
        // 找到所有的起点为当前节点的线的终点即为下一个节点
        List<String> ids = CollectionUtil.filterAndMap(nodeRuntimeDataList,
                nodeData -> nodeRuntimeData.getId().equals(nodeData.getFrom()),
                NodeRuntimeData::getTo);

        if (CollectionUtil.isNotEmpty(ids)) {
            return CollectionUtil.filter(nodeRuntimeDataList,nodeData -> ids.contains(nodeData.getId()));
        }

        return null;
    }

    /**
     * 根据节点运行时数据获取节点实例
     * 过程中节点运行时数据的param构建为节点实例的param
     * @param nodeRuntimeData 节点运行时数据
     * @return 节点实例
     */
    private Node getNodeInstance(NodeRuntimeData nodeRuntimeData) {
        try {
            Class<? extends Node> nodeClazz = NodeTypeEnum.getClazzBynodeRuntimeData(nodeRuntimeData);
            Document params = new Document();
            // 做了修改: 节点参数不为空时才添加
            if (nodeRuntimeData.getParams() != null) {
                params.putAll(nodeRuntimeData.getParams());
            }
            return nodeClazz.getConstructor(Document.class).newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
