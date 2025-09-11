package com.lowcode.workflow.runner.runtime;


import com.lowcode.workflow.runner.node.timer.TimerNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务的运行时调度器
 */
@Slf4j
public class TimeTaskRuntime {
    /**
     * 执行指定运行次数的定时任务
     * @param timerNode 定时任务节点
     */
    public static void runTimeTaskByTimes(TimerNode timerNode) {
        Integer times = timerNode.getTimes();
        AtomicInteger runTaskCount = new AtomicInteger();
        ScheduledFuture<?>[] future = new ScheduledFuture<?>[1];

        Runnable task = () -> {
            if (runTaskCount.getAndIncrement() < times) {
                timerNode.getRunnable().run();
            } else {
                // TODO 做一些校验
                future[0].cancel(false);
            }
        };

        // 获取执行当前定时器的线程池
        ThreadPoolTaskScheduler schedulerThreadPool = RuntimeDataThreadPool.getSchedulerThreadPool(timerNode.getFlowId());

        if (timerNode.getCron() != null) {
            log.info("[提交到定时任务线程池执行固定次数的定时任务]当前定时任务为cron表达式控制的定时任务, cron表达式为: {}, 任务: {}", timerNode.getCron(), timerNode);
            future[0] = schedulerThreadPool.schedule(task, trigger(timerNode));
        } else {
            log.info("[提交到定时任务线程池执行固定次数的定时任务]当前定时任务为固定时间间隔控制的定时任务, 时间间隔为: {} {}, 任务: {}", timerNode.getPeriod(), timerNode.getTimeUnit(), timerNode);
            future[0] = schedulerThreadPool.getScheduledExecutor().scheduleAtFixedRate(task, 0, timerNode.getPeriod(), timerNode.getTimeUnit());
        }

    }

    private static Trigger trigger(TimerNode timerNode) {

        Trigger trigger = null;

        if (timerNode.getCron() != null) {
            trigger = new CronTrigger(timerNode.getCron(), ZoneId.systemDefault());
        } else {
            trigger = new PeriodicTrigger(timerNode.getPeriod(), timerNode.getTimeUnit());
        }

        return trigger;
    }

    /**
     * 执行无限次数的定时任务
     * @param timerNode 定时任务节点
     */
    public static void runTimeTaskAlways(TimerNode timerNode) {
        Runnable task = () -> {
            timerNode.getRunnable().run();
        };
        ThreadPoolTaskScheduler schedulerThreadPool = RuntimeDataThreadPool.getSchedulerThreadPool(timerNode.getFlowId());
        if (timerNode.getCron() != null) {
            log.info("[提交到定时任务线程池执行无限次数的定时任务]当前定时任务为cron表达式控制的定时任务, cron表达式为: {}, 任务: {}", timerNode.getCron(), timerNode);
            schedulerThreadPool.schedule(task, trigger(timerNode));
        } else {
            log.info("[提交到定时任务线程池执行无限次数的定时任务]当前定时任务为固定时间间隔控制的定时任务, 时间间隔为: {} {}, 任务: {}", timerNode.getPeriod(), timerNode.getTimeUnit(), timerNode);
            schedulerThreadPool.getScheduledExecutor().scheduleAtFixedRate(task, 0, timerNode.getPeriod(), timerNode.getTimeUnit());
        }
    }
}
