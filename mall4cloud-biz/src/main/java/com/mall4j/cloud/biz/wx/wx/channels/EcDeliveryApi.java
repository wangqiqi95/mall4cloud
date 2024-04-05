package com.mall4j.cloud.biz.wx.wx.channels;


import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.EcDeliveryRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcDeliveryResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4.0物流接口集合
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcDeliveryApi {
	/**
	 * 获取快递公司列表
	 * @link https://developers.weixin.qq.com/doc/channels/API/order/deliverycompanylist_get.html
	 */
	@POST("/channels/ec/order/deliverycompanylist/get")
	EcDeliveryResponse getDelivery(@Query("access_token") String authorizerAccessToken,@Body EcDeliveryRequest paramRequest);
	
}
