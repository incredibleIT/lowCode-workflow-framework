package com.lowcode.workflow.web.node.entity;


import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * 节点参数Bean
 */
@Data
public class NodeTypeParam {

    private Long id;
    private Long typeId;
    private String key;
    private String name;
    private boolean required;
    /** 参数输入类型: input | select */
    private String inType = "input";
    /** 下拉选项内容，多个值以逗号分隔 */
    private String option;
    /** 参数值提示性内容 */
    private String placeholder;
}
