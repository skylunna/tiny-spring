package com.tiny.spring.web.server;

import com.tiny.spring.web.filter.Filter;
import com.tiny.spring.web.filter.FilterChain;
import com.tiny.spring.web.http.Handler;
import com.tiny.spring.web.http.TinyRequest;
import com.tiny.spring.web.http.TinyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 *
 *      实现简易Web服务器 (注册与调度)
 */
public class TinyWebServer {

    private final List<Filter> globalFilters = new ArrayList<>();
    private final Map<String, Handler> routeMap = new HashMap<>();

    // 注册过滤器 (顺序决定执行顺序)
    public void addFilter(Filter filter) {
        globalFilters.add(filter);
        System.out.println("📦 注册过滤器: " + filter.getClass().getSimpleName());
    }

    // 注册路由
    public void addRoute(String path, Handler handler) {
        routeMap.put(path, handler);
    }

    // 模拟接受请求
    public void dispatch(TinyRequest request) {

        // 绑定目标处理器 (找不到则默认 404)
        request.setHandler(routeMap.getOrDefault(request.getUrl(), (req, res) -> {
            res.setStatus(404);
            res.setBody("Not Found");
        }));

        TinyResponse response = new TinyResponse();

        // 创建过滤链并执行
        FilterChain chain = new FilterChain(globalFilters);
        chain.doFilter(request, response);

        System.out.println("✅ 响应返回 -> 状态: " + response.getStatus() + ", 内容: " + response.getBody());
        System.out.println("--------------------------------------------------");
    }

}
