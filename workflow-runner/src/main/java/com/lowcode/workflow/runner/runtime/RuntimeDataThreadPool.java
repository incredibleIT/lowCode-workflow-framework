package com.lowcode.workflow.runner.runtime;


import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

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
     * 定时任务调度线程池map, 一个流程对应一个定时任务调度线程池
     */
    private static final Map<String, ThreadPoolTaskScheduler> SCHEDULER_POOL_MAP = new ConcurrentHashMap<>();


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
     * 获取定时任务调度线程池
     * @param flowId 流程Id
     * @return 定时任务调度线程池
     */
    public static ThreadPoolTaskScheduler getSchedulerThreadPool(String flowId) {
        ThreadPoolTaskScheduler schedulerTaskPool = null;

        if (SCHEDULER_POOL_MAP.containsKey(flowId)) {
            // 证明当前流程已经存在有调度线程池
            schedulerTaskPool = SCHEDULER_POOL_MAP.get(flowId);
        } else {
            // 当前流程不存在有调度线程池, 则创建一个
            schedulerTaskPool = new ThreadPoolTaskScheduler();
            schedulerTaskPool.setPoolSize(3);
            schedulerTaskPool.setThreadNamePrefix("scheduler-task-");
            schedulerTaskPool.initialize();

            SCHEDULER_POOL_MAP.put(flowId, schedulerTaskPool);
        }

        return schedulerTaskPool;
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
