/*
 * work_wx
 * wuhen 2020/1/16.
 * Copyright (c) 2020  jianfengwuhen@126.com All Rights Reserved.
 */

package com.mall4j.cloud.biz.wx.wx.util;

import com.mall4j.cloud.biz.wx.wx.exception.ErrorMsg;
import com.mall4j.cloud.biz.wx.wx.exception.SessionRequestMsg;
import com.mall4j.cloud.biz.wx.wx.exception.SuccessMsg;
import okhttp3.*;
import org.apache.catalina.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Iterator;

public class RequestUtil {
    private final static Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static RequestBody getRequestBody(String json) {
        return RequestBody.create( JSON, json);
    }


    public static String getPostAdd(String url,String token) {
        return url + "?" + "access_token=" + token;
    }



    public String requestJsonPost(String url,String token,String json) throws IOException {
            Response response = new OkHttpClient().newCall(new Request.Builder().url(
                    RequestUtil.getPostAdd(url, token)
                    ).post(
                    RequestUtil.getRequestBody(json)).build()
            ).execute();
            if (response.code() == 200) {
                return response.body().string();
            }
        return null;
    }


    public SessionRequestMsg requestJsonPostDone(String url, String token, String json) {
        try {
            String response = requestJsonPost(url, token, json);
            if (!StringUtils.isEmpty(response)) {
                return new SuccessMsg(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorMsg(0,e.getMessage());
        }
        return new ErrorMsg(0);
    }


    public String requestGet(String url, String token, ParameterMap parameterMap) throws IOException {
        url = url + "?" +  "access_token=" + token;
        if (null != parameterMap && parameterMap.size() > 0) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                url = url + "&" + key +"=" + parameterMap.get(key);
            }
        }
        Response response = new OkHttpClient().newCall(new Request.Builder().url(url).get().build()).execute();
        if (response.code() == 200) {
            return response.body().string();
        }
        return null;
    }


    public SessionRequestMsg requestGettDone(String url, String token, ParameterMap parameterMap) {
        try {
            String response = requestGet(url, token, parameterMap);
            if (!StringUtils.isEmpty(response)) {
                return new SuccessMsg(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorMsg(0,e.getMessage());
        }
        return new ErrorMsg(0);
    }




}
