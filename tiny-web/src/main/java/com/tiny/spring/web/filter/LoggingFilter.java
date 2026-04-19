package com.tiny.spring.web.filter;


import com.tiny.spring.web.http.TinyRequest;
import com.tiny.spring.web.http.TinyResponse;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 *
 *      日志过滤器 (记录耗时)
 */
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(TinyRequest request, TinyResponse response, FilterChain chain) {
        System.out.println("🔍 [LoggingFilter] 请求进入: " + request.getUrl());
        long start = System.currentTimeMillis();

        chain.doFilter(request, response); // 放行

        long cost = System.currentTimeMillis() - start;
        System.out.println("🔍 [LoggingFilter] 请求结束, 耗时: " + cost + "ms");
    }
}
