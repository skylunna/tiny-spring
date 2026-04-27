package com.tiny.spring.web.filter;

import com.tiny.spring.web.http.TinyRequest;
import com.tiny.spring.web.http.TinyResponse;

/**
 * @author: skylunna
 * @data: 2026/04/19
 *
 * @description:
 *      web - Servlet - Filter
 */
public interface Filter {

    void doFilter(TinyRequest request, TinyResponse response, FilterChain chain);
}
