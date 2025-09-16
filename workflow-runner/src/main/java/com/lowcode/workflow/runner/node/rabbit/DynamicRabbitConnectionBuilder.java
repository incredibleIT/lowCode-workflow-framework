package com.lowcode.workflow.runner.node.rabbit;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * 构建rabbitmq的连接以及rabbitAdmin和rabbitTemplate的构建器
 */
@Slf4j
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


    private DynamicRabbitConnectionBuilder(String baseUrl, String port, String username, String password) {
        log.debug("初始化动态rabbitmq连接, baseUrl: {}, port: {}, username: {}, password: {}", baseUrl, port, username, password);
        log.debug("构建连接工厂, rabbitTemplate, rabbitAdmin");
        this.connectionFactory = createConnectionFactory(baseUrl, port, username, password);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setEncoding("UTF-8");
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = new RabbitAdmin(connectionFactory);
    }

    /**
     * 创建连接工厂
     * TODO 这里没有对virtualHost进行处理, 默认为"/"
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

    public static class Builder {
        private String host;

        private String port;

        private String username;

        private String password;

        private String virtualHost = "/";

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(String port) {
            this.port = port;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public DynamicRabbitConnectionBuilder build() {
            return new DynamicRabbitConnectionBuilder(host, port, username, password);
        }
    }


    public static Builder builder() {
        return new Builder();
    }
}
