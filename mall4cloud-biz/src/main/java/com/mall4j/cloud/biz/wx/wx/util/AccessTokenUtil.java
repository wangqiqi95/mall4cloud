package com.mall4j.cloud.biz.wx.wx.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;

/**
 * 重置accseeToken,api,jsApi
 * @author eury
 *
 */
public class AccessTokenUtil {
	private final static Logger LOG = LoggerFactory.getLogger(AccessTokenUtil.class);
	private final static String requestUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String api_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=wx_card";
	private static final String jsapi_ticket_url="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	/**
	 * 获取jsapiTicket和apiTicket
	 * @param accessToken
	 * @return
	 */
	public static Map<String,String> getApiTicket(String accessToken) {
		Map<String,String> data = new HashMap<String,String>();
		//获取api(卡券用)
		try {
			if(accessToken == null) {
				return null;
			}
			String apiUrl = api_ticket_url.replace("ACCESS_TOKEN", accessToken);
			JSONObject jsonObjApi = WechatUtils.doGetstr(apiUrl);
			LOG.info("AccseeToken response jsonObjApi={}.", new Object[]{jsonObjApi});
			if(jsonObjApi != null){
				String apiTicket = jsonObjApi.getString("ticket");
				data.put("apiTicket", apiTicket);
			}
			//获取jsapi(JS-SDK用)
			String jsApiUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accessToken);
			JSONObject jsonObjJsApi = WechatUtils.doGetstr(jsApiUrl);
			LOG.info("AccseeToken response jsonObjJsApi={}.", new Object[]{jsonObjJsApi});
			if(jsonObjJsApi != null){
				String jsApiTicket = jsonObjJsApi.getString("ticket");
				data.put("jsApiTicket", jsApiTicket);
			}
			data.put("status", "true");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "false");
		}
		return data;
	}
}
