package com.lowcode.workflow.runner;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@DubboComponentScan(basePackages = {"com.lowcode.workflow.runner.data"})
@ServletComponentScan
@EnableDubbo
public class WorkflowRunnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowRunnerApplication.class, args);
    }
}
