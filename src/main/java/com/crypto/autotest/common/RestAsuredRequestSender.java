package com.crypto.autotest.common;

import com.crypto.autotest.baseObjects.ResponseData;
import com.crypto.autotest.interfaces.IRequestSender;

import java.util.HashMap;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class RestAsuredRequestSender extends RequestSender implements IRequestSender {
    public RestAsuredRequestSender(String url, String method, String body) {
        super(url, method, body);
    }

    public RestAsuredRequestSender(String url, String method, String body,String timeForWait) {
        super(url,method, body,timeForWait);
    }

    public RestAsuredRequestSender() {
        super();
    }


    @Override
    public HashMap<String, String> sendRequest(String url, String body) {
        RestAssured.baseURI = url;
        RequestSpecification httpRequest = RestAssured.given();
        Response response = null;
        switch (this.getMethod()){
            case "GET":
                response=httpRequest.request(Method.GET, "");
                break;
            case "POST":
                response=httpRequest.body(body).request(Method.POST, "");
                break;
            case "DELETE":
                response=httpRequest.request(Method.DELETE, "");
                break;
            case "PUT":
                response=httpRequest.body(body).request(Method.PUT, "");
                break;
            case "PATCH":
                response=httpRequest.request(Method.PATCH, "");
                break;
            default:
                response=httpRequest.request(Method.HEAD, "");

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Integer.toString(response.getStatusCode()),response.getBody().toString());
        return map;
    }

    @Override
    public ResponseData sendRequest(){
        RestAssured.baseURI = this.getUrl();
        RequestSpecification httpRequest = RestAssured.given();
        Response response = null;
        switch (this.getMethod()){
            case "GET":
                response=httpRequest.request(Method.GET, "");
                break;
            case "POST":
                response=httpRequest.body(this.getBody()).request(Method.POST, "");
                break;
            case "DELETE":
                response=httpRequest.request(Method.DELETE, "");
                break;
            case "PUT":
                response=httpRequest.body(this.getBody()).request(Method.PUT, "");
                break;
            case "PATCH":
                response=httpRequest.request(Method.PATCH, "");
                break;
            default:
                response=httpRequest.request(Method.HEAD, "");

        }

        int statusCode=response.getStatusCode();
        String responseBody=response.getBody().toString();
        return new ResponseData(statusCode, responseBody);
    }
}
