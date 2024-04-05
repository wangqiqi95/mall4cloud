package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.request.EcFreightTemplateOneRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcFreightTemplateRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcFreightTemplateOneResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcFreightTemplateResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4.0运费模板接口集合
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcFreightApi {
	/**
	 * 查询运费模版
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/getfreighttemplatedetail.html
	 */
	@POST("/channels/ec/merchant/getfreighttemplatedetail")
	EcFreightTemplateOneResponse get(@Query("access_token") String authorizerAccessToken, @Body EcFreightTemplateOneRequest ecFreightTemplateOneRequest);
	
	/**
	 * 添加运费模板
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/addfreighttemplate.html
	 */
	@POST("/channels/ec/merchant/addfreighttemplate")
	EcFreightTemplateResponse add(@Query("access_token") String authorizerAccessToken, @Body EcFreightTemplateRequest ecFreightTemplateRequest);
	
	/**
	 * 更新运费模板
	 * @link https://developers.weixin.qq.com/doc/channels/API/merchant/updatefreighttemplate.html
	 */
	@POST("/channels/ec/merchant/updatefreighttemplate")
	EcFreightTemplateResponse update(@Query("access_token") String authorizerAccessToken, @Body EcFreightTemplateRequest ecFreightTemplateRequest);
	
}
