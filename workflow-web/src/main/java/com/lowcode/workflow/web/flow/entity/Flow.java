package com.lowcode.workflow.web.flow.entity;

import lombok.Data;

import java.util.Date;

/**
 * 流程Bean
 */
@Data
public class Flow {
    private String id;
    /** 流程名称 */
    private String name;
    /** 流程描述 */
    private String description;
    /** 创建人 */
    private String username;
    /** 流程状态 */
    private Status status;
    /** 创建时间 */
    private Date createDate;
    /** 更新时间 */
    private Date updateDate;

    public enum Status {
        /** 未运行 */
        INIT,
        /** 运行中 */
        RUNNING,
        /** 运行完成 */
        FINISHED,
        /** 运行失败 */
        FAILED
    }
}
