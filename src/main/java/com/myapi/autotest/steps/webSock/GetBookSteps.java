package com.myapi.autotest.steps.webSock;

import com.myapi.autotest.baseObjects.ResponseData;
import com.myapi.autotest.common.RequestSender;
import com.myapi.autotest.interfaces.IRequestSender;
import com.myapi.autotest.util.JsonUtil;
import com.myapi.autotest.util.TestCaseUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.HashMap;

public class GetBookSteps {
    private static IRequestSender requestSender=null;
    private JsonUtil jsonUtil=null;
    private HashMap<String,String> inputsMap=new HashMap<>();
    private static ResponseData response=null;

    @Given("user get input parameters")
    public void userGetParameters(){
        inputsMap=new HashMap<>();
        inputsMap.put("book","book");
        inputsMap.put("channel","BTCUSD-PERP");
        inputsMap.put("count","10");

    }

    @When("user send socket request and get response by the file {string}")
    public void sendRequestAndGetResponse(String requestFile){
        IRequestSender reqestSender= TestCaseUtil.getRequestData(inputsMap,"happyFlowSockParameterized.properties");
        String requestBody=((RequestSender)reqestSender).getBody();
        System.out.println(requestBody);
        ResponseData response=reqestSender.sendRequest();
        System.out.println(response.getBody());
        inputsMap.clear();

    }
}
