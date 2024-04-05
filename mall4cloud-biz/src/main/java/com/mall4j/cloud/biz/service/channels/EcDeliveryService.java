package com.mall4j.cloud.biz.service.channels;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.biz.dto.channels.EcCompanyInfo;
import com.mall4j.cloud.api.biz.dto.channels.request.EcDeliveryRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcDeliveryResponse;
import com.mall4j.cloud.biz.wx.wx.channels.EcDeliveryApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.biz.vo.LiveLogisticsVO;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class EcDeliveryService {
	private static final String CHANNELS_DELIVERY_LIST = "CHANNELS_DELIVERY_LIST";
	@Autowired
	private WxConfig wxConfig;
	
	@Autowired
	private EcDeliveryApi ecDeliveryApi;
	
	/**
	 * 查询视频号4.0基本物流列表
	 * @param query
	 * @return
	 */
	public List<LiveLogisticsVO> deliveryList(String query) {
		// 尝试从缓存获取
		EcDeliveryResponse companyList = RedisUtil.get(CHANNELS_DELIVERY_LIST, 3600, () -> {
			EcDeliveryRequest ecDeliveryRequest = new EcDeliveryRequest();
			// 调用微信接口获取视频号4.0物流公司列表
			EcDeliveryResponse ecDeliveryApiDelivery = ecDeliveryApi.getDelivery(wxConfig.getWxEcToken(),ecDeliveryRequest);
			log.info("获取视频号4.0物流公司列表结束，请求参数{}, 执行结果{}",JSONObject.toJSONString(ecDeliveryRequest),JSONObject.toJSONString(ecDeliveryApiDelivery));
			if (ecDeliveryApiDelivery != null) {
				if (ecDeliveryApiDelivery.getErrcode() != 0) {
					log.error("调用微信接口获取视频号4.0物流公司列表异常 token={}, res={}", wxConfig.getWxEcToken(), JSON.toJSONString(ecDeliveryApiDelivery));
					throw new LuckException("获取视频号4.0物流公司列表失败, 错误信息:" + ecDeliveryApiDelivery.getErrmsg());
				}
			}
			return ecDeliveryApiDelivery;
		});
		
		if (companyList == null) {
			throw new LuckException("调用微信接口获取视频号4.0物流公司列表异常");
		}
		
		List<LiveLogisticsVO> result = new ArrayList<>();
		
		for (EcCompanyInfo company : companyList.getCompanyList()) {
			LiveLogisticsVO vo = new LiveLogisticsVO();
			vo.setDeliveryCompanyId(company.getDeliveryId());
			vo.setDeliveryCompanyName(company.getDeliveryName());
			if (StringUtils.isNotBlank(query)) {
				if (company.getDeliveryId().contains(query) || company.getDeliveryName().contains(query)) {
					result.add(vo);
				}
				continue;
			}
			result.add(vo);
		}
		return result;
	}
}
