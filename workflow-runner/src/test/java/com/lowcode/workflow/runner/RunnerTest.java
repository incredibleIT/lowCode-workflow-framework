package com.lowcode.workflow.runner;


import com.lowcode.workflow.common.api.NodeRuntimeDataService;
import com.lowcode.workflow.runner.node.rabbit.RabbitMQMetaGetterService;
import com.lowcode.workflow.runner.runtime.NodeRuntimeDataRunning;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RunnerTest {

    @Autowired
    private NodeRuntimeDataRunning nodeRuntimeDataRunning;


    @Autowired
    private NodeRuntimeDataService nodeRuntimeDataService;


    @Test
    public void test01() {
        new RabbitMQMetaGetterService("guest", "guest").getAllBinding();
        new RabbitMQMetaGetterService("guest", "guest").getAllQueue();
        new RabbitMQMetaGetterService("guest", "guest").getAllExchange();
    }
}
