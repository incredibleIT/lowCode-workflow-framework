package com.lowcode.workflow.runner.node.rabbit;

import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitBinding;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitExchange;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitQueue;
import lombok.Builder;
import lombok.Data;
import org.bson.Document;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RabbitMQNode extends DefaultNode {

    private String BASE_URL = "rabbitmq://localhost:15672/api";

    private String username = "guest";

    private String password = "guest";

    private String exchange;

    private String queue;

    private String routingKey;

    private String message;

    private String binding;

    private final RabbitMQMetaGetterService rabbitMQMetaGetterService = new RabbitMQMetaGetterService(username, password);
    private final DynamicRabbitConnectionBuilder dynamicRabbitConnectionBuilder = new DynamicRabbitConnectionBuilder(BASE_URL, "15672", username, password);


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

        String username = params.getString("username");
        String password = params.getString("password");
        String baseUrl = params.getString("baseUrl");
        String exchange = params.getString("exchange");
        String queue = params.getString("queue");
        String routingKey = params.getString("routingKey");
        String message = params.getString("message");
        String binding = params.getString("binding");


        this.BASE_URL = baseUrl;
        this.username = username;
        this.password = password;
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
        this.message = message;
        this.binding = binding;

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
        // 先查询出所有需要的信息
        List<RabbitExchange> exchangeList = rabbitMQMetaGetterService.getAllExchange();
        List<RabbitQueue> queueList = rabbitMQMetaGetterService.getAllQueue();
        List<RabbitBinding> bindingList = rabbitMQMetaGetterService.getAllBinding();
        // 首先应该去判断一下用户选择的exchange的类型
        RabbitExchange rabbitExchange = rabbitMQMetaGetterService.getExchangeByName(exchange);
        String exchangeType = rabbitExchange.getType();
        // rabbit对于exchange, queue, binding的操作
        RabbitAdmin rabbitAdmin = dynamicRabbitConnectionBuilder.getRabbitAdmin();
        // 用于发送消息
        RabbitTemplate rabbitTemplate = dynamicRabbitConnectionBuilder.getRabbitTemplate();


        if ("".equals(exchangeType)) {
            // 判断是复用了队列, 还是自己需要新建队列
            if (CollectionUtil.noneMatch(bindingList,
                    rabbitBinding -> rabbitBinding.getSource().equals(rabbitExchange.getName()) && rabbitBinding.getDestination().equals(queue))) {
                // 没有匹配的就去新建队列
                rabbitAdmin.declareQueue(new Queue(queue, true, false, false, null));
            }
            rabbitTemplate.convertAndSend(exchange, queue, message);
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


        }

//        else if ("headers".equals(exchangeType)) {
//            // 不做发送
//        } else if ("direct".equals(exchangeType)) {
//
//        } else if ("topic".equals(exchangeType)) {
//
//        }







        callback.callback(putToNextNodeInput());
    }
}
