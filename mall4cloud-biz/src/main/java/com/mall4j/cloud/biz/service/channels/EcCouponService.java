package com.mall4j.cloud.biz.service.channels;

import com.alibaba.fastjson.JSONObject;
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
import com.mall4j.cloud.biz.wx.wx.channels.EcCouponApi;
import com.mall4j.cloud.biz.config.WxConfig;
import com.mall4j.cloud.common.exception.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EcCouponService {
	@Autowired
	private EcCouponApi ecCouponApi;
	@Autowired
	private WxConfig wxConfig;

	public EcCouponResponse couponCreate(EcCouponCreateRequest couponCreateRequest){
		log.info("创建视频号4.0优惠券，参数:{}", JSONObject.toJSONString(couponCreateRequest));
		EcCouponResponse ecCouponResponse = ecCouponApi.couponCreate(wxConfig.getWxMaToken(), couponCreateRequest);
		log.info("创建视频号4.0优惠券，参数:{},执行结果:{}",JSONObject.toJSONString(couponCreateRequest),JSONObject.toJSONString(ecCouponResponse));
		if(ecCouponResponse==null || ecCouponResponse.getErrcode()!=0 ){
			Assert.faild("视频号创建优惠券失败");
		}
		return ecCouponResponse;
	}
	
	
	public EcCouponResponse couponUpdate(EcCouponUpdateRequest couponUpdateRequest){
		log.info("更新视频号4.0优惠券，参数:{}", JSONObject.toJSONString(couponUpdateRequest));
		EcCouponResponse ecCouponResponse = ecCouponApi.couponUpdate(wxConfig.getWxMaToken(), couponUpdateRequest);
		log.info("更新视频号4.0优惠券，参数:{},执行结果:{}",JSONObject.toJSONString(couponUpdateRequest),JSONObject.toJSONString(ecCouponResponse));
		if(ecCouponResponse==null || ecCouponResponse.getErrcode()!=0 ){
			Assert.faild("视频号更新优惠券失败");
		}
		return ecCouponResponse;
	}
	
	
	public EcBaseResponse couponUpdateStatus(EcCouponUpdateStatusRequest couponUpdateStatusRequest){
		log.info("更新视频号4.0优惠券状态，参数:{}", JSONObject.toJSONString(couponUpdateStatusRequest));
		EcBaseResponse ecBaseResponse = ecCouponApi.couponUpdateStatus(wxConfig.getWxMaToken(), couponUpdateStatusRequest);
		log.info("更新视频号4.0优惠券状态，参数:{},执行结果:{}",JSONObject.toJSONString(couponUpdateStatusRequest),JSONObject.toJSONString(ecBaseResponse));
		if(ecBaseResponse==null || ecBaseResponse.getErrcode()!=0 ){
			Assert.faild("更新视频号4.0优惠券状态失败");
		}
		return ecBaseResponse;
	}
	
	public EcCouponGetResponse couponGet(EcCouponData data){
		log.info("获取视频号4.0优惠券详情，参数:{}", JSONObject.toJSONString(data));
		EcCouponGetResponse ecCouponGetResponse = ecCouponApi.couponGet(wxConfig.getWxMaToken(), data);
		log.info("获取视频号4.0优惠券详情，参数:{},执行结果:{}",JSONObject.toJSONString(data),JSONObject.toJSONString(ecCouponGetResponse));
		if(ecCouponGetResponse==null || ecCouponGetResponse.getErrcode()!=0 ){
			Assert.faild("获取视频号4.0优惠券详情失败");
		}
		return ecCouponGetResponse;
	}
	
	
	public EcCouponListResponse couponList(EcCouponListRequest couponListRequest){
		log.info("获取视频号4.0优惠券ID列表，参数:{}", JSONObject.toJSONString(couponListRequest));
		EcCouponListResponse ecCouponListResponse = ecCouponApi.couponList(wxConfig.getWxMaToken(), couponListRequest);
		log.info("获取视频号4.0优惠券ID列表，参数:{},执行结果:{}",JSONObject.toJSONString(couponListRequest),JSONObject.toJSONString(ecCouponListResponse));
		if(ecCouponListResponse==null || ecCouponListResponse.getErrcode()!=0 ){
			Assert.faild("获取视频号4.0优惠券ID列表失败");
		}
		return ecCouponListResponse;
	}
	
	public EcUserCouponListResponse userCouponList(EcUserCouponListRequest userCouponListRequest){
		log.info("获取视频号4.0用户优惠券ID列表，参数:{}", JSONObject.toJSONString(userCouponListRequest));
		EcUserCouponListResponse ecUserCouponListResponse = ecCouponApi.userCouponList(wxConfig.getWxMaToken(), userCouponListRequest);
		log.info("获取视频号4.0用户优惠券ID列表，参数:{},执行结果:{}",JSONObject.toJSONString(userCouponListRequest),JSONObject.toJSONString(ecUserCouponListResponse));
		if(ecUserCouponListResponse==null || ecUserCouponListResponse.getErrcode()!=0 ){
			Assert.faild("获取视频号4.0用户优惠券ID列表失败");
		}
		return ecUserCouponListResponse;
	}
	
	public EcGetUserCouponResponse getUserCoupon(EcGetUserCouponRequest ecGetUserCouponRequest){
		log.info("获取视频号4.0用户优惠券详情，参数:{}", JSONObject.toJSONString(ecGetUserCouponRequest));
		EcGetUserCouponResponse getUserCouponResponse = ecCouponApi.getUserCoupon(wxConfig.getWxMaToken(), ecGetUserCouponRequest);
		log.info("获取视频号4.0用户优惠券详情，参数:{},执行结果:{}",JSONObject.toJSONString(ecGetUserCouponRequest),JSONObject.toJSONString(getUserCouponResponse));
		if(getUserCouponResponse==null || getUserCouponResponse.getErrcode()!=0 ){
			Assert.faild("获取视频号4.0用户优惠券详情失败");
		}
		return getUserCouponResponse;
	}
}
