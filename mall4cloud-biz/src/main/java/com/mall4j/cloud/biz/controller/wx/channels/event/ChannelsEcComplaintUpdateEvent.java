package com.mall4j.cloud.biz.controller.wx.channels.event;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.biz.controller.wx.live.event.INotifyEvent;
import com.mall4j.cloud.biz.service.channels.EcAftersaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 视频号4.0 纠纷更新通知回调
 */
@Slf4j
@Service
public class ChannelsEcComplaintUpdateEvent implements INotifyEvent, InitializingBean {
	
	private static final String method = "channels_ec_complaint_update";

	@Autowired
	EcAftersaleService ecAftersaleService;
	

	/**
	 * https://developers.weixin.qq.com/doc/channels/API/brand/callback/brand_event.html
	 *
	 * {
	 *     "ToUserName": "gh_*",
	 *     "FromUserName": "OpenID",
	 *     "CreateTime": 1662480000,
	 *     "MsgType": "event",
	 *     "Event": "channels_ec_complaint_update",
	 *     "finder_shop_complaint": {
	 *         "complaint_status": 100,
	 *         "after_sale_order_id": "1234567",
	 *         "complaint_id": "1234567",
	 *         "order_id":"123244"
	 *     }
	 * }
	 *
	 * 枚举-EcComplaintInfoStatus
	 * 值	说明
	 * 100	待商家处理纠纷
	 * 101	待客服处理
	 * 102	取消客服介入
	 * 103	客服处理中
	 * 104	待用户补充凭证
	 * 105	用户已补充凭证
	 * 106	待商家补充凭证
	 * 107	商家已补充凭证
	 * 108	待双方补充凭证
	 * 109	双方补充凭证超时
	 * 110	待商家确认
	 * 111	商家申诉中
	 * 112	调解完成
	 * 113	待客服核实
	 * 114	重新退款中
	 * 115	退款核实完成
	 * 116	调解关闭
	 * 305	用户补充凭证超时
	 * 307	商家补充凭证超时
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
		String finderShopComplaintStr = jsonObject.getString("finder_shop_complaint");
		JSONObject finderShopComplaintJson = JSONObject.parseObject(finderShopComplaintStr);

		Integer complaint_status = finderShopComplaintJson.getInteger("complaint_status");
		Long complaint_id = finderShopComplaintJson.getLongValue("complaint_id");
		Long after_sale_order_id = finderShopComplaintJson.getLongValue("after_sale_order_id");
		Long order_id = finderShopComplaintJson.getLongValue("order_id");
		//100	待商家处理纠纷
		if(complaint_status==100){
			log.info("待商家处理纠纷");
		}else if(complaint_status==101){
			//101	待客服处理

		}else if(complaint_status==102){
			//102	取消客服介入

		}else if(complaint_status==103){
			//103	客服处理中

		}else if(complaint_status==104){
			//104	待用户补充凭证

		}else if(complaint_status==105){
			//105	用户已补充凭证

		}else if(complaint_status==106){
			//106	待商家补充凭证

		}else if(complaint_status==107){
			//107	商家已补充凭证

		}else if(complaint_status==108){
			//108	待双方补充凭证

		}else if(complaint_status==109){
			//109	双方补充凭证超时

		}else if(complaint_status==110){
			//110	待商家确认

		}else if(complaint_status==111){
			//111	商家申诉中

		}else if(complaint_status==112){
			//112	调解完成

		}else if(complaint_status==113){
			//113	待客服核实

		}else if(complaint_status==114){
			//114	重新退款中

		}else if(complaint_status==115){
			//115	退款核实完成

		}else if(complaint_status==116){
			//116	调解关闭

		}else if(complaint_status==305){
			//305	用户补充凭证超时

		}else if(complaint_status==307){
			//307	商家补充凭证超时

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
