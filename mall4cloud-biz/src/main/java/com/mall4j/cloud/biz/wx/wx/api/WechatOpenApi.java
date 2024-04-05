package com.mall4j.cloud.biz.wx.wx.api;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.wx.wx.util.WechatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Date 2021年12月31日, 0031 10:59
 */
public class WechatOpenApi {

    private static Logger logger = LoggerFactory.getLogger(WechatOpenApi.class);
    private static String api_create_preauthcode_url = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
    private static String api_component_token_url = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
    private static String get_access_token_bycode_url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?appid=APPID&code=CODE&grant_type=authorization_code&component_appid=COMPONENT_APPID&component_access_token=COMPONENT_ACCESS_TOKEN";
    private static String api_query_auth_url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx";
    public static String send_message_url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    private static String api_authorizer_token_url = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_ACCESS_TOKEN";
    private static String api_get_authorizer_info_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_ACCESS_TOKEN";
    private static String api_get_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";
    private static String api_set_authorizer_option_url = "https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=COMPONENT_ACCESS_TOKEN";

    public static JSONObject getApiQueryAuthInfo(String component_appid, String authorization_code, String component_access_token) throws YAMLException {
        String requestUrl = api_query_auth_url.replace("xxxx", component_access_token);
        JSONObject obj = new JSONObject();
        obj.put("component_appid", component_appid);
        obj.put("authorization_code", authorization_code);
        System.out.println("-------------------3、使用授权码换取公众号的授权信息---requestUrl------------------------" + requestUrl);
        JSONObject result = WechatUtils.doPoststr(requestUrl, obj.toString());
        if (result.containsKey("errcode")) {
            logger.error("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
            throw new YAMLException("获取第三方平台access_token！errcode=" + result.getString("errcode") + ",errmsg = " + result.getString("errmsg"));
        } else {
            return result;
        }
    }

    public static String sendMessage(JSONObject json, String ACCESS_TOKEN) {
        logger.info("--------发送客服消息---------json-----" + json.toString()+"----ACCESS_TOKEN---"+ACCESS_TOKEN);
        String url = send_message_url.replace("ACCESS_TOKEN", ACCESS_TOKEN);
        JSONObject jsonObject = WechatUtils.doPoststr(url, json.toString());
        logger.info("--------发送客服消息---------back-----" + jsonObject.toString());
        return jsonObject.toString();
    }

    public static String sendTextMsg(String content,String touser,String access_token){
        JSONObject obj = new JSONObject();
        Map<String,Object> msgMap = new HashMap<String,Object>();
        msgMap.put("content", content);
        obj.put("touser", touser);
        obj.put("msgtype", "text");
        obj.put("text", msgMap);
        return WechatOpenApi.sendMessage(obj, access_token);
    }

    public static String sendImgMsg(String mediaId,String touser,String access_token){
        JSONObject obj = new JSONObject();
        Map<String,Object> msgMap = new HashMap<String,Object>();
        msgMap.put("media_id", mediaId);
        obj.put("touser", touser);
        obj.put("msgtype", "image");
        obj.put("image", msgMap);
        return WechatOpenApi.sendMessage(obj, access_token);
    }

    public static String sendMiniprogrampageMsg(String title,String appid,String pagepath,String thumbMediaId,String touser,String access_token){
        logger.info("发送小程序消息 小程序路径 【{}】",pagepath);
        JSONObject obj = new JSONObject();
        Map<String,Object> msgMap = new HashMap<String,Object>();
        msgMap.put("title", title);
        msgMap.put("appid", appid);
        msgMap.put("pagepath", pagepath);
        if(StringUtils.isNotEmpty(thumbMediaId)){
            msgMap.put("thumb_media_id", thumbMediaId);
        }
        obj.put("touser", touser);
        obj.put("msgtype", "miniprogrampage");
        obj.put("miniprogrampage", msgMap);
        return WechatOpenApi.sendMessage(obj, access_token);
    }
}
