package com.myapi.autotest.common;

import com.myapi.autotest.baseObjects.ResponseData;
import com.myapi.autotest.interfaces.IRequestSender;

import java.net.URI;
import java.util.HashMap;
import java.net.http.WebSocket;
import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebSocketRequestSender extends RequestSender implements IRequestSender {
    //private static final long TIMEOUT_SCONDS=30;
    public WebSocketRequestSender(String url, String method, String body) {
        super(url, method, body);
    }

    public WebSocketRequestSender(String url, String method,String body,String timeForWait) {
        super(url,method,body,timeForWait);
    }

    public WebSocketRequestSender() {
        super();
    }

    @Override
    public HashMap<String, String> sendRequest(String url, String body) {
       return null;
    }



    public ResponseData sendRequest(){
        WebSockListener listener=null;
        String responseStr="";

        try {
            listener=new WebSockListener(this.getUrl());
            System.out.println("now connecting...");
            listener.connect();
            System.out.println("now waiting for connected...");
            boolean connected=listener.waitForConnection(10, TimeUnit.SECONDS);
            if(!connected){
                System.out.println("connect failed after 10 seconds");
                return null;
            }
            if(!listener.isConnected()){
                System.out.println("websocket connection lost");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        listener.send(this.getBody());
        System.out.println("wait for response exceed more than "+Integer.parseInt(this.getTimeForWait())+" seconds");
        StringBuilder allResponses=new StringBuilder();
        allResponses.append("request:").append(this.getBody());
        allResponses.append("\n\n");
        allResponses.append("response:");
        long startTime=System.currentTimeMillis();
        long timeoutMs=Integer.parseInt(this.getTimeForWait())*1000;
        try {
            while(System.currentTimeMillis()-startTime<timeoutMs) {
                String message = listener.waitForMessage(1, TimeUnit.SECONDS);

                if (message != null) {
                    
                    allResponses.append(message).append("\n");
                    if (message.contains("result\":")) {
                        System.out.println(message.contains("result\":"));
                        allResponses.append("\nsuccessfully get data:");
                        responseStr=message;
                    }

                }
                if((System.currentTimeMillis()-startTime)%5000<1000){
                    System.out.println("waiting..."+(timeoutMs-(System.currentTimeMillis()-startTime))/1000+" in rest");
                }

            }
        } catch (InterruptedException e) {
                throw new RuntimeException(e);
        }

        listener.close();
        if(System.currentTimeMillis()-startTime>=timeoutMs){
            System.out.println("timeout");
        }



        System.out.println("get last responseStr:"+responseStr);

        return new ResponseData(responseStr);

    }

}
