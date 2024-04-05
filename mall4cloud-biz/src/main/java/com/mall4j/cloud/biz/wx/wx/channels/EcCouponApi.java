package com.mall4j.cloud.biz.wx.wx.channels;

import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitClient;
import com.mall4j.cloud.api.biz.dto.channels.EcCouponData;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCouponCreateRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCouponListRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCouponUpdateRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcCouponUpdateStatusRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcGetUserCouponRequest;
import com.mall4j.cloud.api.biz.dto.channels.request.EcUserCouponListRequest;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcCouponGetResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcCouponListResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcCouponResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcGetUserCouponResponse;
import com.mall4j.cloud.api.biz.dto.channels.response.EcUserCouponListResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * 视频号4.0优惠券接口
 *
 */
@RetrofitClient(baseUrl = "https://api.weixin.qq.com")
public interface EcCouponApi {
	
	/**
	 * 创建优惠券
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/create.html
	 */
	@POST("/channels/ec/coupon/create")
	EcCouponResponse couponCreate(@Query("access_token") String authorizerAccessToken, @Body EcCouponCreateRequest couponCreateRequest);
	
	/**
	 * 更新优惠券内容
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/update.html
	 */
	@POST("/channels/ec/coupon/update")
	EcCouponResponse couponUpdate(@Query("access_token") String authorizerAccessToken, @Body EcCouponUpdateRequest couponUpdateRequest);
	
	/**
	 * 更新优惠券状态
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/update_status.html
	 */
	@POST("/channels/ec/coupon/update_status")
	EcBaseResponse couponUpdateStatus(@Query("access_token") String authorizerAccessToken, @Body EcCouponUpdateStatusRequest couponUpdateStatusRequest);
	
	/**
	 * 获取优惠券详情
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/get.html
	 */
	@POST("/channels/ec/coupon/get")
	EcCouponGetResponse couponGet(@Query("access_token") String authorizerAccessToken, @Body EcCouponData data);
	
	/**
	 * 获取优惠券ID列表
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/get_list.html
	 */
	@POST("/channels/ec/coupon/get_list")
	EcCouponListResponse couponList(@Query("access_token") String authorizerAccessToken, @Body EcCouponListRequest couponListRequest);
	
	/**
	 * 获取用户优惠券ID列表
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/get_user_coupon_list.html
	 *
	 * 每次请求的页码间隔不能超过10。
	 * 第一次请求的页码要小于10，或者是最后一页。
	 * 一页最大200
	 */
	@POST("/channels/ec/coupon/get_user_coupon_list")
	EcUserCouponListResponse userCouponList(@Query("access_token") String authorizerAccessToken, @Body EcUserCouponListRequest userCouponListRequest);
	
	/**
	 * 获取用户优惠券详情
	 * @link https://developers.weixin.qq.com/doc/channels/API/coupon/get_user_coupon.html
	 */
	@POST("/channels/ec/coupon/get_user_coupon")
	EcGetUserCouponResponse getUserCoupon(@Query("access_token") String authorizerAccessToken, @Body EcGetUserCouponRequest ecGetUserCouponRequest);
	
}
