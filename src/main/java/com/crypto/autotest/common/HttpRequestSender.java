package com.crypto.autotest.common;

import com.crypto.autotest.baseObjects.ResponseData;
import com.crypto.autotest.interfaces.IRequestSender;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.*;
import java.time.Duration;
import java.util.HashMap;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;

public class HttpRequestSender extends RequestSender implements IRequestSender {



    public HttpRequestSender(String url, String method, String body) {
        super(url, method, body);
    }

    public HttpRequestSender(String url,String method, String body,String timeForWait) {
        super(url,method ,body,timeForWait);
    }

    public HttpRequestSender() {
        super();
    }

    public HashMap<String,String> sendRequest(String url, String body){
        if(this.getMethod().equalsIgnoreCase("GET")){
            HttpClient client = HttpClient.newHttpClient();
            //InetSocketAddress proxyAddress=new InetSocketAddress("127.0.0.1",10809);
            //Proxy proxy=new Proxy(Proxy.Type.HTTP,proxyAddress);
            //ProxySelector proxySelector=ProxySelector.of(proxyAddress);

            //HttpClient client=HttpClient.newBuilder().proxy(proxySelector).connectTimeout(Duration.ofSeconds(10)).build();

            HttpRequest request=null;
            if (this.getMethod().equalsIgnoreCase("GET")){

                request = HttpRequest.newBuilder()
                        .uri(URI.create(url)).header("content-type","application/json")
                        .GET()
                        .build();
            }
            HttpResponse<String> response =null;
            try {
                response = client.send(request, BodyHandlers.ofString());
                System.out.println("Response status code: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (response != null){
                HashMap<String,String> map = new HashMap<>();
                map.put(Integer.toString(response.statusCode()),response.body().toString());
                return map;
            }
            else{
                return null;
            }
        }
        else{
            String responseBody = null;
            String statusCode = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);

            try {
                // 设置请求的内容
                StringEntity requestBody = new StringEntity(body);
                httpPost.setEntity(requestBody);

                // 发送POST请求
                CloseableHttpResponse response = httpClient.execute(httpPost);

                // 处理响应结果
                HttpEntity responseEntity = response.getEntity();
                responseBody = EntityUtils.toString(responseEntity);
                statusCode = Integer.toString(response.getStatusLine().getStatusCode());


                // 关闭httpClient连接
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseBody != null){
                HashMap<String,String> map = new HashMap<>();
                map.put(statusCode,responseBody);
                return map;
            }
            else{
                return null;
            }
        }

    }

    public ResponseData sendRequest(){

        String responseBody = null;
        int statusCode = 0;
        if(this.getMethod().equalsIgnoreCase("GET")){
            HttpClient client = HttpClient.newHttpClient();

            System.out.println(this.getUrl());
            HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(this.getUrl()))
                        .GET()
                        .build();

            HttpResponse<String> response =null;
            try {
                response = client.send(request, BodyHandlers.ofString());
                statusCode=response.statusCode();
                responseBody=response.body().toString();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (response != null){
                return new ResponseData(statusCode,responseBody);
            }
            else{
                return null;
            }
        }
        else{

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(this.getUrl());

            try {

                StringEntity requestBody = new StringEntity(this.getBody());
                httpPost.setEntity(requestBody);


                CloseableHttpResponse response = httpClient.execute(httpPost);


                HttpEntity responseEntity = response.getEntity();
                responseBody = EntityUtils.toString(responseEntity);
                statusCode = response.getStatusLine().getStatusCode();

                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (responseBody != null){
                return new ResponseData(statusCode,responseBody);
            }
            else{
                return null;
            }
        }
    }

}
