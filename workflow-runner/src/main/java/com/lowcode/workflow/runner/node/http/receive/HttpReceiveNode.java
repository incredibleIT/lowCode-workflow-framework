package com.lowcode.workflow.runner.node.http.receive;

import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import com.lowcode.workflow.runner.node.http.DispatcherServlet;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.bson.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Setter
public class HttpReceiveNode extends DefaultNode {

    private static final long TIMEOUT = 10_000;
    // 动态路由注册表
    private static final Map<String, HttpReceiveNode> ROUTE_REGISTRY = new ConcurrentHashMap<>();
    private static final List<String> METHOD_LIST =
            Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS","HEAD");

    private String uri;
    private String method;
    private HttpServletResponse response;

    // 用于阻塞等待请求
    private transient CountDownLatch latch;

    private transient boolean finished = false;

    private transient CallbackFunction callback;

    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public HttpReceiveNode(Document params) {
        super(params);
//        // 激活Servlet
//        DispatcherServlet.activate();
    }

    /**
     * 校验并赋值节点参数
     * @param params 参数
     */
    @Override
    protected void verify(Document params) {
        uri = getUriInParams(params);
        method = getMethodInParams(params);
        // 注册路由
        ROUTE_REGISTRY.put(getMappingKey(uri, method), this);
        log.info("路由注册成功: {} {}", method, uri);
    }

    /**
     * 从参数中获取method
     * @param params 参数
     * @return method
     */
    private String getMethodInParams(Document params) {
        String method = params.getString("method");
        if (method == null || !METHOD_LIST.contains(method.toUpperCase())) {
            throw new IllegalArgumentException("method is null or not supported");
        }
        return method;
    }

    /**
     * 从参数中获取uri
     * @param params 参数
     * @return uri
     */
    private String getUriInParams(Document params) {
        String uri = params.getString("uri");
        if (uri == null) {
            throw new IllegalArgumentException("uri is null");
        }
        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        return uri;
    }

    @Override
    public void run(CallbackFunction callback) {
        log.info("HttpReceiveNode ready: {} {}", method, uri);
        this.callback = callback;   // 保存回调
        // 每次运行时创建新的 latch
        latch = new CountDownLatch(1);
        try {
            // 等待请求
            boolean received = latch.await(TIMEOUT, TimeUnit.MILLISECONDS);
            if (!received) {
                log.warn("等待请求超时（{} ms），继续执行后续逻辑", TIMEOUT);
            } else {
                log.info("已收到请求，继续执行后续逻辑");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("等待请求被中断", e);
        }
    }

    public static String getMappingKey(String uri, String method) {
        return uri + "_" + method.toUpperCase();
    }

    /**
     * 处理请求
     * @param request 请求
     * @param response 响应
     */
    public void handler(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        try {
            // 解析请求体和查询参数
            Document output = new Document();

            // 解析 body
            if (request.getInputStream() != null) {
                String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
                if (StringUtils.hasText(body)) {
                    output.putAll(Document.parse(body));
                }
            }

            // 解析 queryString
            String queryString = request.getQueryString();
            if (StringUtils.hasText(queryString)) {
                queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());
                String[] params = queryString.split("&");
                for (String param : params) {
                    String[] kv = param.split("=");
                    if (kv.length == 2) output.append(kv[0], kv[1]);
                }
            }

            log.info("解析请求参数成功: {}", output.toJson());
            if (this.callback != null) {
                this.callback.callback(output);
            }
        } catch (Exception e) {
            log.error("Parse request params failed: {}", e.getMessage());
            responseData(new Document("error", "Parse request params failed")
                    .append("message", e.getMessage()).toJson());
        }

        // 阻塞等待响应数据写入完成
        long start = System.currentTimeMillis();
        while (!finished) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
                if (System.currentTimeMillis() - start > TIMEOUT) {
                    throw new RuntimeException("Request timeout");
                }
            } catch (Exception e) {
                responseData(new Document("error", "Request timeout")
                        .append("message", e.getMessage()).toJson());
                break;
            }
        }
        finished = false;

//        //
//        Document input = putToNextNodeInput();
//        input.append("requestbody",)


        // 通知 run() 继续执行
        if (latch != null) {
            latch.countDown();
        }
    }

    /**
     * 响应数据
     * @param json 响应数据
     */
    public void responseData(String json) {
        try {
            if (response != null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(json);
                response.flushBuffer();
            }
        } catch (Exception e) {
            log.error("响应数据失败: {}", e.getMessage());
        } finally {
            finished = true;
        }
    }

    /** 全局查找节点 */
    public static HttpReceiveNode findNode(String uri, String method) {
        return ROUTE_REGISTRY.get(getMappingKey(uri, method));
    }


}
