package com.crypto.autotest.steps.restAssured;

import com.crypto.autotest.baseObjects.ResponseData;
import com.crypto.autotest.common.RequestSender;
import com.crypto.autotest.interfaces.IRequestSender;
import com.crypto.autotest.util.JsonUtil;
import com.crypto.autotest.util.TestCaseUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class GetCandleStackSteps {

    private static ResponseData response=null;
    private static Properties properties=null;
    private static IRequestSender requestSender=null;
    private JsonUtil jsonUtil=null;


    public boolean validateDifference(Object[] arr,long interval){
        if(arr==null || arr.length<=1){
            System.out.println("no need to verify");
            return true;
        }
        for(int i=0;i<arr.length-1;i++){
            long current=((Number)arr[i]).longValue();
            long next=((Number)arr[i+1]).longValue();
            long difference=next-current;

            if(difference!=interval){
                System.out.println("validation failed. Current timestamp is "+current+", next timestamp is "+next);
                return false;
            }
        }
        return true;


    }


    @Given("user send request and get response by the file {string}")
    public void sendRequestAndGetResponse(String requestFile){
        requestSender= TestCaseUtil.getRequestData(requestFile);
        //send request
        response=requestSender.sendRequest();

    }

    @Then("User verify status code is {int}")
    public void userVerifyStatusCode(int statusCode)
    {
        int actualStatusCode=response.getStatusCode();
        Assert.assertEquals(actualStatusCode,statusCode);
    }

    @When("user get jsonpath file by {string}")
    public void userGetJsonpathFile(String file)
    {
        properties=TestCaseUtil.loadJsonPathPropertiesFile(file);
    }

    @Then("user verify method is {string}")
    public void userVerifyMethod(String methodName)
    {
        String url =((RequestSender)requestSender).getUrl();
        System.out.println(url);

        String methodPath=properties.getProperty(methodName);
        //get method value in response body
        jsonUtil=new JsonUtil(response.getBody());
        String method=jsonUtil.getValueByJsonPath(methodPath);

        String pathList[]=url.split("/");
        int length=pathList.length;
        System.out.println(pathList[length-1]);
        String api_name=pathList[length-1].split("\\?")[0];
        String full_api_name=pathList[length-2]+"/"+api_name;
        //verify if method value is correct
        Assert.assertEquals(full_api_name,method);
    }

    @Then("user verify parameters is correct in the response body")
    public void verifyParameters(){
        String instrumentNamePath=properties.getProperty("instrument_name");
        String instrumentName=jsonUtil.getStringValueByJsonPath(instrumentNamePath);
        String pathList[]=((RequestSender)requestSender).getUrl().split("/");
        int length=pathList.length;
        String parameters[]=pathList[length-1].split("\\?")[1].split("&");
        HashMap<String,String> map=new HashMap<>();
        for (int i=0;i<parameters.length;i++){
            String key=parameters[i].split("=")[0];
            String value=parameters[i].split("=")[1];
            map.put(key,value);
        }
        //verify instrument_name is correct
        Assert.assertEquals(instrumentName,map.get("instrument_name"));

        String intervalPath=properties.getProperty("interval");
        String interval=jsonUtil.getValueByJsonPath(intervalPath);
        //verify interval is in accordance with the value in request parameter
        Assert.assertEquals(interval,map.get("timeframe"));
    }

    @Then("user verify interval among each item in response is correct")
    public void verifyTimeInterval(){
        String pathList[]=((RequestSender)requestSender).getUrl().split("/");
        int length=pathList.length;
        String parameters[]=pathList[length-1].split("\\?")[1].split("&");
        HashMap<String,String> map=new HashMap<>();
        for (int i=0;i<parameters.length;i++){
            String key=parameters[i].split("=")[0];
            String value=parameters[i].split("=")[1];
            map.put(key,value);
        }


        String tPath=properties.getProperty("startTime");
        List<Object> timeList=jsonUtil.getObjectsByJsonPath(tPath);
        Object[] objects=timeList.toArray();

        for (int i=0;i<objects.length;i++){
            System.out.println(objects[i]);
        }

        String intervalPace=map.get("timeframe");

        long pace=TestCaseUtil.getTimeInterval(intervalPace);
        System.out.println(pace);
        //verify the interval among every 2 objects is correct
        Assert.assertTrue(validateDifference(objects,pace));
    }

    @Then("user verify error code {string}")
    public void verifyErrCode(String errCode){
        Assert.assertTrue(response.getBody().contains(errCode));
    }

    @Then("user verify error message {string}")
    public void verifyErrMsg(String errMsg){
        Assert.assertTrue(response.getBody().contains(errMsg));
    }
}
