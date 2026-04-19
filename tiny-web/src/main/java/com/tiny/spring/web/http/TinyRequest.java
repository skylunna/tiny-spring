package com.tiny.spring.web.http;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 */
public class TinyRequest {

    private String url;

    private Map<String, String> headers = new HashMap<>();
    private Object body;
    private Handler handler;
}
