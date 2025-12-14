package com.myapi.autotest;
import com.myapi.autotest.baseObjects.ResponseData;
import com.myapi.autotest.common.RequestSender;
import com.myapi.autotest.interfaces.IRequestSender;
import com.myapi.autotest.util.JsonUtil;
import com.myapi.autotest.util.TestCaseUtil;
import org.junit.Assert;
import org.testng.annotations.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
public class WebSocketTest {
    @BeforeMethod
    public void SetUp(){
        System.out.println("setUp");
    }
    @AfterMethod
    public void TearDown(){
        System.out.println("tearDown");
    }



    @Test
    public void testBook(){
        IRequestSender reqestSender= TestCaseUtil.getRequestData("happyFlowSock.properties");
        String requestBody=((RequestSender)reqestSender).getBody();
        ResponseData response=reqestSender.sendRequest();
        System.out.println(response.getBody());

        Properties request_properties=TestCaseUtil.loadJsonPathPropertiesFile("bookRequest.properties");
        String methodPath_request=request_properties.getProperty("method");
        //get method value in request body
        JsonUtil jsonUtilRequest=new JsonUtil(requestBody);
        String method_request=jsonUtilRequest.getValueByJsonPath(methodPath_request);

        Properties response_properties=TestCaseUtil.loadJsonPathPropertiesFile("book.properties");
        String methodPath_response=response_properties.getProperty("method");
        //get method value in response body
        JsonUtil jsonUtilResponse=new JsonUtil(response.getBody());
        String method_response=jsonUtilResponse.getValueByJsonPath(methodPath_response);

        //verify method value is correct
        Assert.assertEquals(method_request,method_response);

        //get channels values from request
        String channelsPath_request=request_properties.getProperty("channel");
        String channels_request=jsonUtilRequest.getStringValueByJsonPath(channelsPath_request);
        String[] channels_expected=channels_request.split("\\.");
        String channel_request=channels_expected[0];
        String instrumentNameRequest=channels_expected[1];
        String depth_request=channels_expected[2];

        //verify channel is correct
        String channelPath_response=response_properties.getProperty("channel");
        String channel_response=jsonUtilResponse.getStringValueByJsonPath(channelPath_response);
        Assert.assertEquals(channel_request,channel_response);

        //verify depth is correct
        String depthPath_response=response_properties.getProperty("depth");
        String depth_response=jsonUtilResponse.getStringValueByJsonPath(depthPath_response);
        Assert.assertEquals(depth_request,depth_response);

        //verify instrument name is correct
        String instrumentNamePath_response=response_properties.getProperty("instrument_name");
        String instrumentNameResponse=jsonUtilResponse.getStringValueByJsonPath(instrumentNamePath_response);
        Assert.assertEquals(instrumentNameRequest,instrumentNameResponse);

        //verify subscription value is in accordance with channe value string from request
        String subscription_path=response_properties.getProperty("subscription");
        String subscription=jsonUtilResponse.getStringValueByJsonPath(subscription_path);


        for(int i=0;i<channels_request.length();i++){

            Assert.assertEquals(channels_request.charAt(i),subscription.charAt(i));
        }


        //verify asks table length of each row is 3
        String asks_path=response_properties.getProperty("asks");
        List<List<Object>> table=jsonUtilResponse.getTableByJsonPath(asks_path);


        if(table.get(0).size()>0){
            System.out.println(table.get(0).get(0).toString());
            String arr[]=table.get(0).get(0).toString().split(",");
            Assert.assertEquals(arr.length,3);
        }

        //verify t exists
        String t_path=response_properties.getProperty("t");
        String t=jsonUtilResponse.getStringValueByJsonPath(t_path);
        Assert.assertTrue(t!=null);

        //verify tt exists
        String tt_path=response_properties.getProperty("tt");
        String tt=jsonUtilResponse.getStringValueByJsonPath(tt_path);
        Assert.assertTrue(tt!=null);

        //verify u exists
        String u_path=response_properties.getProperty("u");
        String u=jsonUtilResponse.getStringValueByJsonPath(u_path);
        Assert.assertTrue(u!=null);

        //verify bids if exists
        String bids_path=response_properties.getProperty("bids");
        table=jsonUtilResponse.getTableByJsonPath(bids_path);


        if(table.get(0).size()>0){
            System.out.println(table.get(0).get(0).toString());
            String arr[]=table.get(0).get(0).toString().split(",");
            Assert.assertEquals(arr.length,3);

        }

    }

    @Test
    public void testBookForParameterized(){
        HashMap<String,String> inputsMap=new HashMap<>();
        inputsMap.put("book","book");
        inputsMap.put("channel","BTCUSD-PERP");
        inputsMap.put("count","10");
        IRequestSender reqestSender= TestCaseUtil.getRequestData(inputsMap,"happyFlowSockParameterized.properties");
        String requestBody=((RequestSender)reqestSender).getBody();
        System.out.println(requestBody);
        String url=((RequestSender)reqestSender).getUrl();
        System.out.println(url);
        ResponseData response=reqestSender.sendRequest();
        System.out.println(response.getBody());
    }
}
