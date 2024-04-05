package com.mall4j.cloud.biz.service.channels;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.request.EcFreightTemplateOneRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcFreightTemplateRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcFreightTemplateOneResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcFreightTemplateResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcFreightApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EcFreightService {
	
	@Autowired
	private WxConfig wxConfig;
	
	@Autowired
	private EcFreightApi ecFreightApi;
	
	public EcFreightTemplateOneResponse get(EcFreightTemplateOneRequest ecFreightTemplateOneRequest){
		return ecFreightApi.get(wxConfig.getWxEcToken(),ecFreightTemplateOneRequest);
	}
	
	public EcFreightTemplateResponse add(EcFreightTemplateRequest ecFreightTemplateRequest){
		log.info("调用微信接口添加视频号运费模板参数对象：{}", JSONObject.toJSONString(ecFreightTemplateRequest));
		EcFreightTemplateResponse freightTemplateResponse = ecFreightApi.add(wxConfig.getWxEcToken(), ecFreightTemplateRequest);
		log.info("调用微信接口添加视频号运费模板执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecFreightTemplateRequest),JSONObject.toJSONString(freightTemplateResponse));
		if (freightTemplateResponse != null) {
			if (freightTemplateResponse.getErrcode() != 0) {
				log.error("调用微信接口添加视频号运费模板异常 req{}, res={}", JSON.toJSONString(ecFreightTemplateRequest), JSON.toJSONString(freightTemplateResponse));
				throw new LuckException("调用微信接口添加视频号运费模板异常, 错误信息:" + freightTemplateResponse.getErrmsg());
			}
		}
		return freightTemplateResponse;
	}
	
	public EcFreightTemplateResponse update(EcFreightTemplateRequest ecFreightTemplateRequest){
		log.info("调用微信接口更新视频号运费模板参数对象：{}", JSONObject.toJSONString(ecFreightTemplateRequest));
		EcFreightTemplateResponse freightTemplateResponse = ecFreightApi.update(wxConfig.getWxEcToken(), ecFreightTemplateRequest);
		log.info("调用微信接口更新视频号运费模板执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecFreightTemplateRequest),JSONObject.toJSONString(freightTemplateResponse));
		if (freightTemplateResponse != null) {
			if (freightTemplateResponse.getErrcode() != 0) {
				log.error("调用微信接口更新视频号运费模板异常 req{}, res={}", JSON.toJSONString(ecFreightTemplateRequest), JSON.toJSONString(freightTemplateResponse));
				throw new LuckException("调用微信接口更新视频号运费模板异常, 错误信息:" + freightTemplateResponse.getErrmsg());
			}
		}
		
		return freightTemplateResponse;
	}
}
