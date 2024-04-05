package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.dto.channels.event.BrandAuditDTO;
import com.mall4j.cloud.biz.service.channels.ChannelsBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 视频号4.0品牌资质事件回调
 */
@Slf4j
@Service
public class ChannelsEcBrandEvent implements INotifyEvent, InitializingBean {
	
	private static final String method = "channels_ec_brand";
	
	@Autowired
	private ChannelsBrandService channelsBrandService;
	
	/**
	 * https://developers.weixin.qq.com/doc/channels/API/brand/callback/brand_event.html
	 *
	 * {
	 *     "ToUserName":"gh_*",
	 *     "FromUserName":"OPENID",
	 *     "CreateTime":1662480000,
	 *     "MsgType":"event",
	 *     "Event":"channels_ec_brand",
	 *     "BrandEvent": {
	 *         "brand_id": "10002934",
	 *         "audit_id": "12345678",
	 *         "status":3,
	 *         "reason":""
	 *     }
	 * }
	 *
	 * 回调状态枚举-status
	 * 1	新增品牌
	 * 2	更新品牌
	 * 3	撤回品牌审核
	 * 4	审核成功
	 * 5	审核失败
	 * 6	删除品牌
	 * 7	品牌资质被系统撤销
	 *
	 *
	 * @param postData
	 * @return
	 * @throws Exception
	 */
	@Override
	public String doEvent(String postData) throws Exception {
		log.info("视频号4.0品牌资质事件回调，输入参数：{}", postData);
		
		JSONObject jsonObject =  JSONObject.parseObject(postData);
		BrandAuditDTO brandAuditDTO = jsonObject.getObject("BrandEvent", BrandAuditDTO.class);
		
		if(Objects.nonNull(brandAuditDTO)){
			log.info("视频号4.0品牌资质回调审核参数：{}", brandAuditDTO);
			channelsBrandService.updateBrandEvent(brandAuditDTO);
		}
		return "success";
	}
	
	@Override
	public void register(String event, INotifyEvent notifyEvent) {
		INotifyEvent.super.register(event, notifyEvent);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.register(method,this);
	}
}
