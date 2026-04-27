package com.tiny.spring.web.filter;

import com.tiny.spring.web.http.TinyRequest;
import com.tiny.spring.web.http.TinyResponse;

import java.util.List;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 *
 *      负责按顺序调用过滤器，并在所有过滤器执行完后调用目标 Handler
 */
public class FilterChain {

    // 所有过滤器的集合 (比如编码过滤器、登陆过滤器、日置过滤器)
    private final List<Filter> filters;

    // 当前执行到第几个过滤器
    private int index = 0;

    // 构造时传入所有过滤器
    public FilterChain(List<Filter> filters) {
        this.filters = filters;
    }

    // 执行过滤器链的下一个过滤器
    // 如果没有过滤器了，就执行业务逻辑
    public void doFilter(TinyRequest request, TinyResponse response) {
        if (index < filters.size()) {
            // 1. 取出当前过滤器
            Filter currentFilter = filters.get(index++);

            // 2. 执行过滤器逻辑 (过滤器内不会决定是否调用 chain.doFilter)
            currentFilter.doFilter(request, response, this);
        } else {
            // 3. 所有过滤器执行完毕，调用目标业务逻辑
            System.out.println("🎯 到达目标处理器: " + request.getUrl());
            request.getHandler().handle(request, response);
        }
    }

}
