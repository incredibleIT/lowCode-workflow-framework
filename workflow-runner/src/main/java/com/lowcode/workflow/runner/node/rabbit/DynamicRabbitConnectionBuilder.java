package com.lowcode.workflow.runner.node.rabbit;


import lombok.Getter;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Caching;

/**
 * 动态构建rabbitmq的连接
 */
public class DynamicRabbitConnectionBuilder {

    /**
     * 连接工厂
     */
    private final CachingConnectionFactory connectionFactory;

    /**
     * 发布消息
     */
    @Getter
    private final RabbitTemplate rabbitTemplate;

    /**
     * 管理交换机、队列、绑定
     */
    @Getter
    private final RabbitAdmin rabbitAdmin;


    public DynamicRabbitConnectionBuilder(String baseUrl, String port, String username, String password) {

        this.connectionFactory = createConnectionFactory(baseUrl, port, username, password);
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitAdmin = new RabbitAdmin(connectionFactory);
    }

    /**
     * 创建连接工厂
     */
    private CachingConnectionFactory createConnectionFactory(String host, String port, String username, String password) {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(Integer.parseInt(port));
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");

        return connectionFactory;
    }


    /**
     * 关闭连接
     */
    public void close() {
        if (connectionFactory != null) {
            connectionFactory.destroy();
        }

    }







}
