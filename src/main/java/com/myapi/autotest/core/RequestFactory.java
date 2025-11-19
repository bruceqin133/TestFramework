package com.myapi.autotest.core;

import com.myapi.autotest.common.HttpRequestSender;
import com.myapi.autotest.common.RestAsuredRequestSender;
import com.myapi.autotest.common.WebSocketRequestSender;
import com.myapi.autotest.interfaces.IRequestSender;
import com.myapi.autotest.util.PropertyUtil;

public class RequestFactory {

    private PropertyUtil propertyUtil;
    private IRequestSender requestSender;
    public RequestFactory(){

    }

    public RequestFactory(PropertyUtil propertyUtil){
        this.propertyUtil=propertyUtil;

    }

    public IRequestSender getRequestSender(){
        String requestType=propertyUtil.getPropertyValue("requestType");
        if(requestType.equalsIgnoreCase("http")){
            String url=propertyUtil.getPropertyValue("url");
            String method=propertyUtil.getPropertyValue("method");
            String body="";
            if (method.equalsIgnoreCase("POST")) {
                body=propertyUtil.getPropertyValue("body");
            }
            this.requestSender=new HttpRequestSender(url,method,body);

        }
        else if(requestType.equalsIgnoreCase("webSocket")){
            String url=propertyUtil.getPropertyValue("url");
            String body=propertyUtil.getPropertyValue("body");
            String timeForWait=propertyUtil.getPropertyValue("timeForWait");
            this.requestSender=new WebSocketRequestSender(url,"none",body,timeForWait);
        }
        else{
            String url=propertyUtil.getPropertyValue("url");
            String method=propertyUtil.getPropertyValue("method");
            String body="";
            if (method.equalsIgnoreCase("POST")) {
                body=propertyUtil.getPropertyValue("body");
            }
            this.requestSender=new RestAsuredRequestSender(url,method,body);
        }
        return this.requestSender;
    }


}
