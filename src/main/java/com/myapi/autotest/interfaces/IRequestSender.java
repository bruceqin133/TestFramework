package com.myapi.autotest.interfaces;

import java.util.HashMap;
import com.myapi.autotest.baseObjects.ResponseData;

public interface IRequestSender {
    public HashMap<String,String> sendRequest(String url,String body);

    public ResponseData sendRequest();

}
