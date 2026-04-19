package com.tiny.spring.web.http;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 */
@FunctionalInterface
public interface Handler {

    void handle(TinyRequest request, TinyResponse response);
}
