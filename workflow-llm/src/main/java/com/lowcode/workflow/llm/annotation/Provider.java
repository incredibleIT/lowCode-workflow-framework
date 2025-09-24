package com.lowcode.workflow.llm.annotation;

import java.lang.annotation.*;

/**
 * 标记模型构建器所支持的 provider 名称
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Provider {

    String value();
}
