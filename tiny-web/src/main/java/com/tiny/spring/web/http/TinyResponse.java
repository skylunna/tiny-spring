package com.tiny.spring.web.http;

/**
 * @author: skylunna
 * @data: 2026/04/17
 *
 * @description:
 *      web - Servlet - Filter
 */
public class TinyResponse {

    private int status = 200;

    private Object body;

    // 标记是否已处理完毕
    private boolean completed = false;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
