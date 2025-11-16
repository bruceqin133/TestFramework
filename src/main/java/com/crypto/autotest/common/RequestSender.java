package com.crypto.autotest.common;

public class RequestSender {
    private String url;
    private String method;
    private String body;
    private String timeForWait;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public RequestSender(String url, String method, String body) {
        this.url = url;
        this.method = method;
        this.body = body;
    }

    public RequestSender(String url, String method, String body,String timeForWait) {
        this.url = url;

        this.body = body;

        this.method = method;

        this.timeForWait = timeForWait;


    }

    public RequestSender() {}

    public String getTimeForWait() {
        return timeForWait;
    }

    public void setTimeForWait(String timeForWait) {
        this.timeForWait = timeForWait;
    }
}
