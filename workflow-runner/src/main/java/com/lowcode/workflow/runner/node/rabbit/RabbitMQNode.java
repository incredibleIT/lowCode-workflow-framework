package com.lowcode.workflow.runner.node.rabbit;

import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitBinding;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitExchange;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;

/**
 * rabbitmq 节点
 */
@Slf4j
public class RabbitMQNode extends DefaultNode {

    private String url = "127.0.0.1";

    private String BASE_URL = "http://" + url + ":15672/api";

    private String username = "guest";

    private String password = "guest";

    private String exchange;

    private String queue;

    private String routingKey;

    private String message;

    private String binding;

    private final RabbitMQMetaGetterService rabbitMQMetaGetterService = new RabbitMQMetaGetterService(username, password, this.BASE_URL);
//    private final DynamicRabbitConnectionBuilder dynamicRabbitConnectionBuilder = new DynamicRabbitConnectionBuilder(url, "5672", username, password);
    private final DynamicRabbitConnectionBuilder connectionBuilder =
        DynamicRabbitConnectionBuilder.builder()
                .username(username)
                .password(password)
                .host(url)
                .port("5672")
                .build();

    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public RabbitMQNode(Document params) {
        super(params);
    }

    @Override
    protected void verify(Document params) {
        log.info("正在对RabbitMQ节点进行参数校验以及参数赋值");

        String username = params.getString("username");
        String password = params.getString("password");
        String url = params.getString("url");
        String exchange = params.getString("exchange");
        String queue = params.getString("queue");
        String routingKey = params.getString("routingKey");
        String message = params.getString("message");
        String binding = params.getString("binding");


        this.url = url;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
        this.getInput().put("message", message);
        this.binding = binding;

        log.info("-----------------------RabbitMQ节点参数校验以及参数赋值完成, input: {}", this.getInput());
    }

    /**
     * 假设在运行时需要的参数都由前端传递完成并通过verify校验并赋值了
     * 这里路由器exchange的类型有: topic、fanout、direct、headers
     * fanout情况下路由键是不必要的, topic和direct路由需要路由键和绑定中的路由键
     * 对于选定交换机后
     * @param callback 回调函数
     */
    @Override
    public void run(CallbackFunction callback) {
        log.info("进入到RabbitMq的run运行方法");
        // 先查询出所有需要的信息
        List<RabbitExchange> exchangeList = rabbitMQMetaGetterService.getAllExchange();
        List<RabbitQueue> queueList = rabbitMQMetaGetterService.getAllQueue();
        List<RabbitBinding> bindingList = rabbitMQMetaGetterService.getAllBinding();
        // 首先应该去判断一下用户选择的exchange的类型
        RabbitExchange rabbitExchange = rabbitMQMetaGetterService.getExchangeByName(exchange);
        String exchangeType = rabbitExchange.getType();
        // rabbit对于exchange, queue, binding的操作
        RabbitAdmin rabbitAdmin = connectionBuilder.getRabbitAdmin();
        // 用于发送消息
        RabbitTemplate rabbitTemplate = connectionBuilder.getRabbitTemplate();


        if ("".equals(exchangeType)) {
            // 判断是复用了队列, 还是自己需要新建队列
            if (CollectionUtil.noneMatch(bindingList,
                    rabbitBinding -> rabbitBinding.getSource().equals(rabbitExchange.getName()) && rabbitBinding.getDestination().equals(queue))) {
                // 没有匹配的就去新建队列
                rabbitAdmin.declareQueue(new Queue(queue, true, false, false, null));
            }
//            rabbitTemplate.convertAndSend(exchange, message);
            rabbitTemplate.convertAndSend(exchange, getInput());
        }

        if ("fanout".equals(exchangeType)) {
            // 判断是否复用已经存在的队列
            if (CollectionUtil.noneMatch(bindingList,
                    rabbitBinding -> rabbitBinding.getSource().equals(rabbitExchange.getName()) && rabbitBinding.getDestination().equals(queue))) {
                // 没有匹配的就去新建队列
                rabbitAdmin.declareQueue(new Queue(queue, true, false, false, null));
                // 创建绑定, fanout 类型的交换机会忽略路由键
                rabbitAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, rabbitExchange.getName(), "", null));
            }

//            rabbitTemplate.convertAndSend(exchange, message);
            rabbitTemplate.convertAndSend(exchange, getInput());
        }

        if ("headers".equals(exchangeType)) {
            log.info("headers 类型的交换机暂不作处理");
        }

        if ("direct".equals(exchangeType)) {
            // 判断是否复用已经存在的队列
            if (CollectionUtil.noneMatch(bindingList,
                    rabbitBinding -> rabbitBinding.getSource().equals(rabbitExchange.getName()) && rabbitBinding.getDestination().equals(queue))) {
                // 没有匹配的就去新建队列
                rabbitAdmin.declareQueue(new Queue(queue, true, false, false, null));
                // 创建绑定
                rabbitAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, rabbitExchange.getName(), routingKey, null));
            }

//            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            rabbitTemplate.convertAndSend(exchange, routingKey, getInput());
        }


        if ("topic".equals(exchangeType)) {
            // 判断是否复用已经存在的队列
            if (CollectionUtil.noneMatch(bindingList,
                    rabbitBinding -> rabbitBinding.getSource().equals(rabbitExchange.getName()) && rabbitBinding.getDestination().equals(queue))) {
                // 没有匹配的就去新建队列
                rabbitAdmin.declareQueue(new Queue(queue, true, false, false, null));
                // 创建绑定
                rabbitAdmin.declareBinding(new Binding(queue, Binding.DestinationType.QUEUE, rabbitExchange.getName(), routingKey, null));
            }

//            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            rabbitTemplate.convertAndSend(exchange, routingKey, getInput());
        }
        callback.callback(putToNextNodeInput());
    }
}
