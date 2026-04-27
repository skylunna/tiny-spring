package com.tiny.spring.web.test;

import com.tiny.spring.web.filter.AuthFilter;
import com.tiny.spring.web.filter.LoggingFilter;
import com.tiny.spring.web.http.TinyRequest;
import com.tiny.spring.web.server.TinyWebServer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 *
 *      测试入口
 */
public class TestFilter {
    public static void main(String[] args) {
        TinyWebServer server = new TinyWebServer();

        // 注册过滤器 (注意顺序)
        server.addFilter(new LoggingFilter());
        server.addFilter(new AuthFilter());

        // 注册业务路由
        server.addRoute("/api/user", (req, res) -> {
            res.setBody("用户数据: {id: 1, name: 'Alice'}");
            System.out.println("💼 [Business] 处理用户请求...");
        });

        System.out.println("=== 测试 1：带有效 Token 的请求 ===");
        TinyRequest req1 = new TinyRequest();
        req1.setUrl("/api/user");
        Map<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "valid-token");
        req1.setHeaders(authMap);
        server.dispatch(req1);
        // 手动注入 header（模拟）
//        server.dispatch(req1);
        // 实际项目中 Header 会在 Request 构建时传入，这里为演示简化逻辑，
        // 你可以给 TinyRequest 加个 setHeader 方法测试不同场景。

        System.out.println("\n=== 测试 2：无 Token 的请求（触发拦截） ===");
        // 模拟无 Token 场景（实际可重构 Request 构造器）
        // 这里直接演示拦截逻辑的短路效果
        TinyRequest req2 = new TinyRequest();
        req2.setUrl("/api/user");
        // 不设置 header
        server.dispatch(req2);
    }
}
