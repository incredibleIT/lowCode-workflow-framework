package com.lowcode.workflow.web;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.lowcode.workflow.web.flow.mapper", "com.lowcode.workflow.web.node.data.mapper", "com.lowcode.workflow.web.node.type.mapper", "com.lowcode.workflow.web.node.params.mapper"})
@EnableDubbo
public class WorkflowWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowWebApplication.class, args);
    }
}
