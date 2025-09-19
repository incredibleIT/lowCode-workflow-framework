package com.lowcode.workflow.runner.node.http;

import com.lowcode.workflow.runner.node.http.receive.HttpReceiveNode;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 分发 Servlet，根据请求的 URI 和 HTTP 方法，将请求分发给对应的 HttpReceiveNode 处理。
 */
@Slf4j
@WebServlet(name="eleven",urlPatterns = "/*")  // 注册并映射一个 Servlet 到指定的 URL 路径
public class DispatcherServlet extends HttpServlet {

//    // Servlet启动开关
//    private static volatile boolean ENABLED = false;
//
//    // 定时任务单线程池
//    private static final ScheduledExecutorService SCHEDULED_THREAD_POOL = Executors.newSingleThreadScheduledExecutor();
//
//    // 60秒后关闭
//    private static final long CLOSE_DELAY = 60;

//    public static void activate() {
//        ENABLED = true;
//        // 在指定的延迟时间后自动把它设回 false
//        SCHEDULED_THREAD_POOL.schedule(() -> {
//            ENABLED = false;
//        },CLOSE_DELAY, TimeUnit.SECONDS);
//    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

//        if(!ENABLED) {
//            log.warn("---------- DispatcherServlet is disabled ----------");
//            return;
//        }

        String uri = req.getRequestURI();
        String method = req.getMethod();

        HttpReceiveNode node = HttpReceiveNode.findNode(uri, method);
        if (node != null) {
            node.handler(req, resp);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
