package com.tiny.spring.web.filter;


import com.tiny.spring.web.http.TinyRequest;
import com.tiny.spring.web.http.TinyResponse;

/**
 * @author: skylunna
 * @data: 2026/04/19
 *
 * @description:
 *      web - Servlet - Filter
 *
 *      鉴权过滤器 (支持短路拦截)
 */
public class AuthFilter implements Filter {

    @Override
    public void doFilter(TinyRequest request, TinyResponse response, FilterChain chain) {
        String token = request.getHeaders().get("Authorization");
        if (!"valid-token".equals(token)) {
            System.out.println("🚫 [AuthFilter] 拦截！未提供有效 Token");
            response.setStatus(401);
            response.setBody("Unauthorized");
            response.setCompleted(true);
            // 不调用chain.doFilter() 直接短路返回
            return;
        }

        System.out.println("✅ [AuthFilter] 鉴权通过，放行");
        chain.doFilter(request, response);
    }
}
