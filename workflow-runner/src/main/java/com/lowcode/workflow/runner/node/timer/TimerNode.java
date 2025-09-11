package com.lowcode.workflow.runner.node.timer;


import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import com.lowcode.workflow.runner.runtime.TimeTaskRuntime;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class TimerNode extends DefaultNode {
    /**
     * 时间间隔
     */
    private Long period;

    /**
     * 时间间隔的单位
     */
    private TimeUnit timeUnit;

    /**
     * 执行次数
     */
    private Integer times;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 定时任务
     */
    private Runnable runnable;

    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public TimerNode(Document params) {
        super(params);
    }

    @Override
    protected void verify(Document params) {
        String period = params.getString("period");
        // 间隔时间
        String value = period.substring(0, period.indexOf(","));
        // 时间单位
        String type = period.substring(period.indexOf(",") + 1);
        // 执行次数
        String times = params.getString("times");

        if ("CRON".equals(type)) {
            String cron = params.getString("cron");
            this.cron = cron;
        } else {
            this.period = Long.parseLong(value);
            this.timeUnit = TimeUnit.valueOf(type);
        }
        this.times = Integer.parseInt(times);
    }

    @Override
    public void run(CallbackFunction callback) {

        this.runnable = () -> {
            Document output = putToNextNodeInput();

            callback.callback(output);
        };
        /**
         * 每个TimeNode的run方法只会被调用一次
         * 将runnable提交到定时任务调度器
         */
        if (times > 0) {
            // 指定次数执行
            TimeTaskRuntime.runTimeTaskByTimes(this);
        } else {
            // 无限执行
            TimeTaskRuntime.runTimeTaskAlways(this);
        }
        log.info("TimerNode的run方法被流程执行引擎调用, 节点为{}, 指定的执行次数为{}", this.getNodeId(), this.times);
    }
}
