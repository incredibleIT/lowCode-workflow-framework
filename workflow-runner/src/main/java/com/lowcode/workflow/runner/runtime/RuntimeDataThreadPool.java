package com.lowcode.workflow.runner.runtime;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 运行时线程池管理器, 管理线程池的全生命周期
 */
public class RuntimeDataThreadPool {

    /**
     * 线程池map, 一个流程对应一个线程池
     */
    private static final Map<String, ExecutorService> THREAD_POOL_MAP = new ConcurrentHashMap<>();


    /**
     * 获取流程的线程池
     * @param flowId 流程Id
     * @return 线程池
     */
    public static ExecutorService getThreadPool(String flowId) {
        if (THREAD_POOL_MAP.containsKey(flowId)) {
            return THREAD_POOL_MAP.get(flowId);
        }
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        THREAD_POOL_MAP.put(flowId, cachedThreadPool);
        return cachedThreadPool;
    }

    /**
     * 关闭流程的线程池
     * @param flowId 流程Id
     */
    public static void shutdownThreadPool(String flowId) {
        if (THREAD_POOL_MAP.containsKey(flowId)) {
            THREAD_POOL_MAP.get(flowId).shutdownNow();
            THREAD_POOL_MAP.remove(flowId);
        }
    }

}
