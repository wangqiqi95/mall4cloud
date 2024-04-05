package com.mall4j.cloud.docking.skq_crm.service;

import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponFreezeThawDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponProjectSyncDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponReleaseDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponStateUpdateDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponWriteOffDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponFreezeThawResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponReleaseResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponSingleGetResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CouponGetVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface ICrmCouponService {

	ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGet(CouponGetDto couponGetDto);

	ServerResponseEntity couponProjectSync(CouponProjectSyncDto couponProjectSyncDto);

	ServerResponseEntity<CouponReleaseResp> couponRelease(CouponReleaseDto couponReleaseDto);

	ServerResponseEntity<CouponCheckResp> couponCheck(CouponGetDto couponGetDto);

	ServerResponseEntity couponWriteOff(CouponWriteOffDto couponWriteOffDto);

	ServerResponseEntity<CouponFreezeThawResp> freezeThaw(CouponFreezeThawDto couponFreezeThawDto);

	/**
	 * 方法描述：小程序端创建优惠券推送至CRM
	 * @param couponDetailDTO
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:25:49
	 */
	ServerResponseEntity<Void> couponPush(CouponDetailDTO couponDetailDTO);

	/**
	 * 方法描述：小程序端更新优惠券推送至CRM
	 * @param couponDetailDTO
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:26:13
	 */
	ServerResponseEntity<Void> couponUpdatePush(CouponDetailDTO couponDetailDTO);

	/**
	 * 方法描述：优惠券状态同步
	 * @param couponStateUpdateDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:36:27
	 */
	ServerResponseEntity<Void> couponStateUpdate(CouponStateUpdateDto couponStateUpdateDto);
	
	/**
	 * 方法描述：单个优惠券码查询接口
	 * @param couponCode
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2023-04-19 14:40:27
	 */
	ServerResponseEntity<CouponSingleGetResp> couponSingleGet(String couponCode);
}
