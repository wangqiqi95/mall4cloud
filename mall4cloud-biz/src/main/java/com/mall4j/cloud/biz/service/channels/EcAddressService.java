package com.mall4j.cloud.biz.service.channels;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcAddressDetail;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddressRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAddressResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcAddressApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class EcAddressService {
	@Autowired
	private WxConfig wxConfig;
	
	@Autowired
	private EcAddressApi ecAddressApi;
	
	public EcAddressResponse add(EcAddressRequest ecAddressRequest){
		log.info("提交添加退货地址参数对象：{}", JSONObject.toJSONString(ecAddressRequest));
		EcAddressResponse addressResponse = ecAddressApi.add(wxConfig.getWxEcToken(), ecAddressRequest);
		log.info("提交添加退货地址执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecAddressRequest),JSONObject.toJSONString(addressResponse));
		if (Objects.isNull(addressResponse)) {
			log.error("提交添加退货地址失败, req={}, res={}", JSON.toJSONString(ecAddressRequest), JSON.toJSONString(addressResponse));
			throw new LuckException("提交添加退货地址失败");
		}
		return addressResponse;
	}
	
	public EcAddressResponse get(EcAddressDetail ecAddressDetail){
		return ecAddressApi.get(wxConfig.getWxEcToken(), ecAddressDetail);
	}
	
	public EcBaseResponse update(EcAddressRequest ecAddressRequest){
		log.info("提交更新退货地址参数对象：{}", JSONObject.toJSONString(ecAddressRequest));
		EcBaseResponse baseResponse = ecAddressApi.update(wxConfig.getWxEcToken(), ecAddressRequest);
		log.info("提交更新退货地址执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecAddressRequest),JSONObject.toJSONString(baseResponse));
		if(baseResponse != null) {
			if (baseResponse.getErrcode() != 0) {
				log.error("提交更新退货地址失败, req={}, res={}", JSON.toJSONString(ecAddressRequest), JSON.toJSONString(baseResponse));
				throw new LuckException("提交更新退货地址失败, 错误信息:" + baseResponse.getErrmsg());
			}
		}
		return baseResponse;
	}
	
	public EcBaseResponse delete(EcAddressDetail ecAddressDetail){
		log.info("提交删除退货地址参数对象：{}", JSONObject.toJSONString(ecAddressDetail));
		EcBaseResponse baseResponse = ecAddressApi.delete(wxConfig.getWxEcToken(), ecAddressDetail);
		log.info("提交删除退货地址执行结束，参数对象：{}，执行结果{}", JSONObject.toJSONString(ecAddressDetail),JSONObject.toJSONString(baseResponse));
		if(baseResponse != null) {
			if (baseResponse.getErrcode() != 0) {
				log.error("提交删除退货地址失败, req={}, res={}", JSON.toJSONString(ecAddressDetail), JSON.toJSONString(baseResponse));
				throw new LuckException("提交删除退货地址失败, 错误信息:" + baseResponse.getErrmsg());
			}
		}
		return baseResponse;
	}
}
