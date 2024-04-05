package com.mall4j.cloud.docking.skq_crm.controller;

import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponFreezeThawDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponProjectSyncDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponReleaseDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponStateUpdateDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponWriteOffDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.feign.CrmCouponFeignClient;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponFreezeThawResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponReleaseResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponSingleGetResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CouponGetVo;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "CRM优惠券相关接口")
public class CrmCouponController implements CrmCouponFeignClient {

	@Autowired ICrmCouponService crmCouponService;

	/**
	 * 方法描述：优惠券查询接口
	 *
	 * @param couponGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult < java.util.List < com.mall4j.cloud.api.docking.skq_crm.vo.CouponGetVo>>>
	 * @date 2022-01-25 08:24:38
	 */
	@Override
	@ApiOperation(value = "优惠券查询接口", notes = "优惠券查询接口")
	public ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGet(CouponGetDto couponGetDto) {
		return crmCouponService.couponGet(couponGetDto);
	}

	/**
	 * 方法描述：微信优惠券项目Id同步至CRM接口
	 *
	 * @param couponProjectSyncDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 08:57:30
	 */
	@Override
	@ApiOperation(value = "微信优惠券项目Id同步至CRM接口", notes = "微信优惠券项目Id同步至CRM接口")
	public ServerResponseEntity couponProjectSync(CouponProjectSyncDto couponProjectSyncDto) {
		return crmCouponService.couponProjectSync(couponProjectSyncDto);
	}

	/**
	 * 方法描述：优惠券发放（小程序—>CRM）
	 *
	 * @param couponReleaseDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CouponReleaseResp>
	 * @date 2022-01-26 09:16:26
	 */
	@Override
	@ApiOperation(value = "优惠券发放（小程序—>CRM）", notes = "优惠券发放（小程序—>CRM）")
	public ServerResponseEntity<CouponReleaseResp> couponRelease(CouponReleaseDto couponReleaseDto) {
//		return ServerResponseEntity.success();
		return crmCouponService.couponRelease(couponReleaseDto);
	}

	/**
	 * 方法描述：优惠券验证接口
	 *
	 * @param couponGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CouponCheckResp>
	 * @date 2022-01-26 09:40:04
	 */
	@Override
	public ServerResponseEntity<CouponCheckResp> couponCheck(CouponGetDto couponGetDto) {
		return crmCouponService.couponCheck(couponGetDto);
	}

	/**
	 * 方法描述：优惠券核销接口
	 *
	 * @param couponWriteOffDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 10:20:41
	 */
	@Override
	@ApiOperation(value = "优惠券核销接口", notes = "优惠券核销接口")
	public ServerResponseEntity couponWriteOff(CouponWriteOffDto couponWriteOffDto) {
		return crmCouponService.couponWriteOff(couponWriteOffDto);
	}

	/**
	 * 方法描述：优惠券冻结解冻
	 *
	 * @param couponFreezeThawDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CouponFreezeThawResp>
	 * @date 2022-01-26 12:56:54
	 */
	@Override
	public ServerResponseEntity<CouponFreezeThawResp> freezeThaw(CouponFreezeThawDto couponFreezeThawDto) {
		return crmCouponService.freezeThaw(couponFreezeThawDto);
	}

	/**
	 * 方法描述：小程序端创建优惠券推送至CRM
	 *
	 * @param couponDetailDTO
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:25:49
	 */
	@Override
	public ServerResponseEntity<Void> couponPush(CouponDetailDTO couponDetailDTO) {
		return crmCouponService.couponPush(couponDetailDTO);
	}

	/**
	 * 方法描述：小程序端更新优惠券推送至CRM
	 *
	 * @param couponDetailDTO
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:26:13
	 */
	@Override
	public ServerResponseEntity<Void> couponUpdatePush(CouponDetailDTO couponDetailDTO) {
		return ServerResponseEntity.success();
//		return crmCouponService.couponUpdatePush(couponDetailDTO);
	}

	/**
	 * 方法描述：优惠券状态同步
	 *
	 * @param couponStateUpdateDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:36:27
	 */
	@Override
	public ServerResponseEntity<Void> couponStateUpdate(CouponStateUpdateDto couponStateUpdateDto) {
		return ServerResponseEntity.success();
//		return crmCouponService.couponStateUpdate(couponStateUpdateDto);
	}
	
	/**
	 * 单个优惠券码查询接口
	 * @param couponCode
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2023-04-19 15:00:25
	 */
	@Override
	public ServerResponseEntity<CouponSingleGetResp> couponSingleGet(String couponCode) {
		return crmCouponService.couponSingleGet(couponCode);
	}
}
