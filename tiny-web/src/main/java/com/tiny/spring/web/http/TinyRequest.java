package com.tiny.spring.web.http;

import java.util.HashMap;
import java.util.Map;


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



    public String getUrl() {
        return url;
    }

    public Object getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
