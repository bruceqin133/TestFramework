package com.crypto.autotest.baseObjects;

public class ResponseData {

    private int statusCode;
    private String body;



    public ResponseData()
    {}

    public ResponseData(int statusCode, String body)
    {
        this.statusCode = statusCode;
        this.body = body;
    }

    public ResponseData(String body){
        this.body=body;
    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode=statusCode;
    }
}
