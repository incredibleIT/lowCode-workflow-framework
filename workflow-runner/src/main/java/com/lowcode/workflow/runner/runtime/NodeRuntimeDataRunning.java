package com.lowcode.workflow.runner.runtime;


import com.lowcode.workflow.common.model.NodeRuntimeData;
import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.enu.NodeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class NodeRuntimeDataRunning {


    public void runNodeRuntimeData(List<NodeRuntimeData> nodeRuntimeDataList) {
        String flowId = nodeRuntimeDataList.get(0).getFlowId();
        // 获取所有开始节点, TODO BUG: 一个流中只有是起点的开始节点才合法
        List<NodeRuntimeData> start = CollectionUtil.filter(nodeRuntimeDataList, this::isStart);
        // 获取所有作为起点的定时器节点
        List<NodeRuntimeData> timer = CollectionUtil.filter(nodeRuntimeDataList, nodeRuntimeData -> isFirst(nodeRuntimeData, nodeRuntimeDataList) && isTimer(nodeRuntimeData));

        log.info("----------收集所有的开始节点----------");
        start.forEach(startNode -> log.info("开始节点: {}", startNode));

        log.info("----------收集所有作为起点的定时器节点----------");
        timer.forEach(timerNode -> log.info("定时器节点: {}", timerNode));


        // 调用线程池执行所有的开始节点
        CollectionUtil.foreach(start,
                node -> RuntimeDataThreadPool.getThreadPool(flowId).execute(() -> new RuntimeDataExecutor(nodeRuntimeDataList).start(node)));

        //TODO 调用线程池执行所有的定时器节点
        log.info("-----------将定时任务提交到线程池调度--------------");
    }


    /**
     * 当前运行时节点是否为开始节点
     * @param nodeRuntimeData 当前运行时节点
     * @return true：是开始节点 false：不是开始节点
     */
    private boolean isStart(NodeRuntimeData nodeRuntimeData) {
        return NodeTypeEnum.START.getType().equals(nodeRuntimeData.getType());
    }

    /**
     * 判断当前运行时节点是否为起点节点
     * @param nodeRuntimeData 当前运行时节点
     * @param nodeRuntimeDataList 所有运行时节点
     * @return true：是起点节点 false：不是起点节点
     */
    private boolean isFirst(NodeRuntimeData nodeRuntimeData, List<NodeRuntimeData> nodeRuntimeDataList) {
//        log.info("-----------------当前节点是否为起点节点: {}", nodeRuntimeData);
        return CollectionUtil.noneMatch(nodeRuntimeDataList, node -> Objects.equals(node.getFrom(), nodeRuntimeData.getId()));
    }

    /**
     * 判断当前节点是否为定时器节点
     * @param nodeRuntimeData 当前运行时节点
     * @return true：是定时器节点 false：不是定时器节点
     */
    private boolean isTimer(NodeRuntimeData nodeRuntimeData) {
//        log.info("-----------------当前节点是否为定时器节点: {}", nodeRuntimeData);
        return NodeTypeEnum.TIMER.getType().equals(nodeRuntimeData.getType());
    }
}
