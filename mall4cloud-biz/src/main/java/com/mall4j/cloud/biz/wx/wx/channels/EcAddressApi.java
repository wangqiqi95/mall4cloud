package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.EcAddressDetail;
import com.mall4j.cloud.api.biz.dto.channels.request.EcAddressRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcAddressResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4.0地址管理接口集合
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcAddressApi {
	
	/**
	 * 添加地址
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/address/add.html
	 */
	@POST("/channels/ec/merchant/address/add")
	EcAddressResponse add(@Query("access_token") String authorizerAccessToken, @Body EcAddressRequest ecAddressRequest);
	
	/**
	 * 获取地址详情
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/address/get.html
	 */
	@POST("/channels/ec/merchant/address/get")
	EcAddressResponse get(@Query("access_token") String authorizerAccessToken, @Body EcAddressDetail ecAddressDetail);
	
	/**
	 * 更新地址
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/address/update.html
	 */
	@POST("/channels/ec/merchant/address/update")
	EcBaseResponse update(@Query("access_token") String authorizerAccessToken, @Body EcAddressRequest ecAddressRequest);
	
	/**
	 * 删除地址
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/address/delete.html
	 */
	@POST("/channels/ec/merchant/address/delete")
	EcBaseResponse delete(@Query("access_token") String authorizerAccessToken, @Body EcAddressDetail ecAddressDetail);

}
