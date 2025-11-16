package com.crypto.autotest.util;
import com.jayway.jsonpath.JsonPath;


import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
public class JsonUtil {
    private String jsonContent;

    public JsonUtil(String jsonContent) {
        this.jsonContent = jsonContent;

    }


    public  List<String> getValuesByJsonPath(String jsonPath){
        List<String> values = JsonPath.read(this.getJsonContent(), jsonPath);
        return values;
    }

    public List<List<Object>> getTableByJsonPath(String jsonPath){
        List<List<Object>> table = JsonPath.read(this.getJsonContent(), jsonPath);
        return table;
    }

    public  Map<String,Object> getMapByJsonPath(String jsonPath){
        Map<String,Object> map = JsonPath.read(this.getJsonContent(), jsonPath);
        return map;
    }

    public String getValueByJsonPath(String jsonPath){
        String value=JsonPath.read(this.getJsonContent(),jsonPath);
        return value;
    }

    public String getStringValueByJsonPath(String jsonPath){
        JSONArray values=JsonPath.read(this.getJsonContent(),jsonPath);
        String value=values.get(0).toString();
        //System.out.println(value);
        return value;
    }



    public Object getObjectByJsonPath(String jsonPath){
        Object obj=JsonPath.read(this.getJsonContent(),jsonPath);
        return obj;
    }

    public List<Object> getObjectsByJsonPath(String jsonPath){
        List<Object> objects=JsonPath.read(this.getJsonContent(),jsonPath);
        return objects;
    }


    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
