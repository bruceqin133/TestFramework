package com.myapi.autotest;
import com.myapi.autotest.baseObjects.ResponseData;
import com.myapi.autotest.common.RequestSender;
import com.myapi.autotest.interfaces.IRequestSender;
import com.myapi.autotest.util.JsonUtil;
import com.myapi.autotest.util.TestCaseUtil;
import org.junit.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class RestAssuredTest {

    //verify the difference between each 2 objects in arr is interval
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

    @DataProvider(name="happyFowTestData")
    public Object[][] provideTestDataFiles(){
        return new Object[][]{
                {"happyFlowHttpForDay.properties","get-candlestick.properties"},
                {"happyFlowHttpForHour.properties","get-candlestick.properties"},
                {"happyFlowHttpForDay2.properties","get-candlestick.properties"},
                {"happyFlowHttpReverseParam.properties","get-candlestick.properties"},
                {"happyFlowHttpInject.properties","get-candlestick.properties"}
        };
    }

    @DataProvider(name="noTimeTestData")
    public Object[][] provideNoTimeTestDataFiles(){
        return new Object[][]{
                {"happyFlowHttpNoTimeInput.properties","get-candlestick.properties"},
                {"happyFlowHttpNoTimeParam.properties","get-candlestick.properties"},

        };
    }

    @DataProvider(name="wrongInputData")
    public Object[][] provideTestDataForWrongInputName(){
        return new Object[][]{
                {"wrongInstrumentName.properties",400,"40004","Unsupported instrument exchange"},
                {"wrongInstrumentNullName.properties",400,"40004","Invalid instrument_name"},
                {"wrongInstrumentNullName.properties",400,"40004","Invalid instrument_name"},
                {"wrongInstrumentNameSpecialCharactors.properties",400,"40004","Invalid instrument_name"},
                {"wrongIntervalName.properties",400,"40003","Invalid request"},
                {"wrongIntervalNameNull.properties",400,"40003","Invalid request"},
                {"wrongIntervalNameSpectialCharactor.properties",400,"40003","Invalid request"},
                {"NoParamsHttp.properties",400,"40003","Invalid request"},

        };
    }

    @BeforeMethod
    public void SetUp(){
        System.out.println("setUp");
    }
    @AfterMethod
    public void TearDown(){
        System.out.println("tearDown");
    }

    @Test
    public void testGetCandleStickHappyFlow(){

        System.out.println("send Request");
        //init request from test data
        IRequestSender reqestSender= TestCaseUtil.getRequestData("happyFlowHttp.properties");
        //send request
        ResponseData response=reqestSender.sendRequest();
        //verify status code
        Assert.assertEquals(response.getStatusCode(),200);
        //get response body
        System.out.println(response.getBody());

        //get json path from test data
        Properties properties=TestCaseUtil.loadJsonPathPropertiesFile("get-candlestick.properties");
        String methodPath=properties.getProperty("method");
        //get method value in response body
        JsonUtil jsonUtil=new JsonUtil(response.getBody());
        String method=jsonUtil.getValueByJsonPath(methodPath);


        //get request url
        String url =((RequestSender)reqestSender).getUrl();
        System.out.println(url);

        String pathList[]=url.split("/");
        int length=pathList.length;
        System.out.println(pathList[length-1]);
        String api_name=pathList[length-1].split("\\?")[0];
        String full_api_name=pathList[length-2]+"/"+api_name;
        //verify if method value is correct
        Assert.assertEquals(full_api_name,method);

        String instrumentNamePath=properties.getProperty("instrument_name");
        String instrumentName=jsonUtil.getStringValueByJsonPath(instrumentNamePath);

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

    @Test(dataProvider = "happyFowTestData")
    public void testGetCandleStickHappyFlowForDifferentScenarios(String requestFile,String jsonPathFile){

        System.out.println("send Request");
        //init request from test data
        IRequestSender reqestSender= TestCaseUtil.getRequestData(requestFile);
        //send request
        ResponseData response=reqestSender.sendRequest();
        //verify status code
        Assert.assertEquals(response.getStatusCode(),200);
        //get response body
        System.out.println(response.getBody());

        //get json path from test data
        Properties properties=TestCaseUtil.loadJsonPathPropertiesFile(jsonPathFile);
        String methodPath=properties.getProperty("method");
        //get method value in response body
        JsonUtil jsonUtil=new JsonUtil(response.getBody());
        String method=jsonUtil.getValueByJsonPath(methodPath);


        //get request url
        String url =((RequestSender)reqestSender).getUrl();
        System.out.println(url);

        String pathList[]=url.split("/");
        int length=pathList.length;
        System.out.println(pathList[length-1]);
        String api_name=pathList[length-1].split("\\?")[0];
        String full_api_name=pathList[length-2]+"/"+api_name;
        //verify if method value is correct
        Assert.assertEquals(full_api_name,method);

        String instrumentNamePath=properties.getProperty("instrument_name");
        String instrumentName=jsonUtil.getStringValueByJsonPath(instrumentNamePath);

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


    @Test(dataProvider = "wrongInputData")
    public void testGetCandleStickWrongInput(String requestFile,int statusCode,String errorCode,String errorMessage){
        System.out.println("send Request");
        IRequestSender reqestSender= TestCaseUtil.getRequestData(requestFile);
        //send request
        ResponseData response=reqestSender.sendRequest();
        //verify status code
        Assert.assertEquals(response.getStatusCode(),statusCode);

        String responseBody=response.getBody();

        System.out.println(responseBody);

        Assert.assertTrue(responseBody.contains(errorCode));

        Assert.assertTrue(responseBody.contains(errorMessage));

    }

    @Test
    public void testGetCandleStickViolation(){
        System.out.println("send Request");
        IRequestSender reqestSender= TestCaseUtil.getRequestData("InsecureInstrumentName.properties");
        //send request
        ResponseData response=reqestSender.sendRequest();
        //verify status code
        Assert.assertEquals(response.getStatusCode(),403);

        String responseBody=response.getBody();

        System.out.println(responseBody);

        Assert.assertTrue(responseBody.contains("Sorry, you have been blocked"));

    }

    @Test(dataProvider = "noTimeTestData")
    public void getCandleStickNoTimeData(String requestFile,String jsonPathFile){

        System.out.println("send Request");
        //init request from test data
        IRequestSender reqestSender= TestCaseUtil.getRequestData(requestFile);
        //send request
        ResponseData response=reqestSender.sendRequest();
        //verify status code
        Assert.assertEquals(response.getStatusCode(),200);
        //get response body
        System.out.println(response.getBody());

        //get json path from test data
        Properties properties=TestCaseUtil.loadJsonPathPropertiesFile(jsonPathFile);
        String methodPath=properties.getProperty("method");
        //get method value in response body
        JsonUtil jsonUtil=new JsonUtil(response.getBody());
        String method=jsonUtil.getValueByJsonPath(methodPath);


        //get request url
        String url =((RequestSender)reqestSender).getUrl();
        System.out.println(url);

        String pathList[]=url.split("/");
        int length=pathList.length;
        System.out.println(pathList[length-1]);
        String api_name=pathList[length-1].split("\\?")[0];
        String full_api_name=pathList[length-2]+"/"+api_name;
        //verify if method value is correct
        Assert.assertEquals(full_api_name,method);

        String instrumentNamePath=properties.getProperty("instrument_name");
        String instrumentName=jsonUtil.getStringValueByJsonPath(instrumentNamePath);

        String parameters[]=pathList[length-1].split("\\?")[1].split("&");
        HashMap<String,String> map=new HashMap<>();
        for (int i=0;i<parameters.length;i++){
            if(parameters[i].contains("=")){
                if(parameters[i].split("=").length==2){String key=parameters[i].split("=")[0];
                    String value=parameters[i].split("=")[1];
                    map.put(key,value);}

            }

        }
        //verify instrument_name is correct
        Assert.assertEquals(instrumentName,map.get("instrument_name"));


        String intervalPath=properties.getProperty("interval");
        String interval=jsonUtil.getValueByJsonPath(intervalPath);
        //verify interval is 1m by default
        Assert.assertEquals(interval,"1m");

        String tPath=properties.getProperty("startTime");
        List<Object> timeList=jsonUtil.getObjectsByJsonPath(tPath);
        Object[] objects=timeList.toArray();

        for (int i=0;i<objects.length;i++){
            System.out.println(objects[i]);
        }

        String intervalPace="1m";

        long pace=TestCaseUtil.getTimeInterval(intervalPace);
        System.out.println(pace);
        //verify the interval among every 2 objects is correct
        Assert.assertTrue(validateDifference(objects,pace));

    }
}
