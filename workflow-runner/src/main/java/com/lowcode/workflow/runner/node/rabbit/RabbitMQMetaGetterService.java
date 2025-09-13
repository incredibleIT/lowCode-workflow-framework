package com.lowcode.workflow.runner.node.rabbit;


import com.alibaba.nacos.shaded.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lowcode.workflow.common.utils.CollectionUtil;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitBinding;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitExchange;
import com.lowcode.workflow.runner.node.rabbit.entity.RabbitQueue;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Base64;
import java.util.List;


/**
 * 获取元数据
 */
@Slf4j
public class RabbitMQMetaGetterService {

    private final OkHttpClient httpClient;
    private final String usernameAndPassword;
    private final Gson gson;

    public RabbitMQMetaGetterService(String username, String password) {
        this.httpClient = new OkHttpClient.Builder().build();
        this.usernameAndPassword = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
        this.gson = new Gson();
    }

    /**
     * 获取当前连接的所有存在的交换机
     */
    public void getAllExchange() {
        Request request = getRequest("/exchanges");
        try (Response response = getResponse(request)) {

            String responseBody = response.body().string();
            log.info("(测试) 当前的交换机有: {}", responseBody);
            List<RabbitExchange> rabbitExchangeList = gson.fromJson(responseBody, new TypeToken<List<RabbitExchange>>() {}.getType());

            CollectionUtil.foreach(rabbitExchangeList, rabbitExchange -> log.info("(测试) 当前的交换机有: {}", rabbitExchange.toString()));
        } catch (IOException e) {
            // TODO 做一些处理
            System.out.println(e.getMessage());
        }

    }

    /**
     * 获取当前连接的所有存在的队列
     */
    public void getAllQueue() {
        Request request = getRequest("/queues");
        try (Response response = getResponse(request)) {
            String responseBody = response.body().string();
            log.info("(测试) 当前的队列有: {}", responseBody);
            List<RabbitQueue> rabbitQueueList = gson.fromJson(responseBody, new TypeToken<List<RabbitQueue>>(){}.getType());
            CollectionUtil.foreach(rabbitQueueList, rabbitQueue -> log.info("(测试) 当前的队列有: {}", rabbitQueue.toString()));

        } catch (IOException e) {
            // TODO 做一些处理
            System.out.println(e.getMessage());
        }
    }

    /**
     * 获取当前连接的所有存在的绑定关系
     */
    public void getAllBinding() {
        Request request = getRequest("/bindings");
        try (Response response = getResponse(request)) {
            String responseBody = response.body().string();
            log.info("(测试) 当前的绑定关系有: {}", responseBody);
            List<RabbitBinding> rabbitBindingList = gson.fromJson(responseBody, new TypeToken<List<RabbitBinding>>(){}.getType());
            CollectionUtil.foreach(rabbitBindingList, rabbitBinding -> log.info(("[测试]绑定关系: " + (rabbitBinding.getSource().equals("") ? "(default)": rabbitBinding.getSource()) + " -> " + rabbitBinding.getDestination() + " by " + rabbitBinding.getRouting_key())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Request getRequest(String url) {
        String BASE_URL = "http://127.0.0.1:15672/api";
        return new Request.Builder()
                .url(BASE_URL + url)
                .header("Authorization", "Basic " + this.usernameAndPassword)
                .build();
    }

    private Response getResponse(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }

}
