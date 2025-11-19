package com.myapi.autotest.util;

import com.myapi.autotest.core.RequestFactory;
import com.myapi.autotest.interfaces.IRequestSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestCaseUtil {
    public static IRequestSender getRequestData(String fileName){
        PropertyUtil propertyUtil = new PropertyUtil();
        propertyUtil.loadPropertiesFromFile("/testCaseData/"+fileName);
        RequestFactory requestFactory = new RequestFactory(propertyUtil);
        IRequestSender requestSender=requestFactory.getRequestSender();
        return requestSender;
    }

    public static String getValueByRegex(String content,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String value = matcher.group(1);
            System.out.println("find target value:"+value);
            return value;// 输出: def
        }
        return null;
    }

    public static Properties loadJsonPathPropertiesFile(String fileName){
        PropertyUtil propertyUtil = new PropertyUtil();
        Properties properties=propertyUtil.loadPropertiesFromFile("/jsonPath/" + fileName);
        return properties;
    }

    public static ArrayList<IRequestSender> getRequestDataList(ArrayList<String> fileList){
        ArrayList<IRequestSender> senderList = new ArrayList<IRequestSender>();
        for (int i=0;i< fileList.size();i++){
            IRequestSender requestSender = getRequestData(fileList.get(i));
            senderList.add(requestSender);
        }
        return senderList;
    }

    public static long getTimeInterval(String interval){
        int length=interval.length();
        long interval_pace=0;
        if(!Character.isDigit(interval.charAt(0))){
            String range=interval.charAt(0)+"";

            int num=Integer.parseInt(interval.substring(1));
            switch (range){
                case "M":
                    interval_pace=num*60*1000;
                    break;
                case "H":
                    interval_pace=num*3600*1000;
                    break;
                case "D":
                    interval_pace=num*3600*24*1000;
                    break;
                default:
                    interval_pace=0;

            }


        }
        else{
            int num=Integer.parseInt(interval.substring(0,interval.length()-1));
            String range=interval.charAt(length-1)+"";
            switch (range){
                case "d":
                    interval_pace=num*24*3600*1000;
                    break;
                case "m":
                    interval_pace=num*60*1000;
                    break;
                default:
                    interval_pace=0;

            }
        }
        return interval_pace;
    }
}
