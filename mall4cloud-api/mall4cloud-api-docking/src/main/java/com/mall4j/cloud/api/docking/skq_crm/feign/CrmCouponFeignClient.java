package com.mall4j.cloud.api.docking.skq_crm.feign;

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
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 类描述：CRM 优惠券接口
 *
 * @date 2022/1/24 17:20：17
 */
@FeignClient(value = "mall4cloud-docking",contextId = "crm-coupon")
public interface CrmCouponFeignClient {

	/**
	 * 方法描述：优惠券查询接口
	 * @param couponGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult<java.util.List<com.mall4j.cloud.api.docking.skq_crm.vo.CouponGetVo>>>
	 * @date 2022-01-25 08:24:38
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/get")
	ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGet(@RequestBody CouponGetDto couponGetDto);

	/**
	 * 方法描述：微信优惠券项目Id同步至CRM接口
	 * @param couponProjectSyncDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 08:57:30
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/projectSync")
	ServerResponseEntity couponProjectSync(@RequestBody CouponProjectSyncDto couponProjectSyncDto);

	/**
	 * 方法描述：优惠券发放（小程序—>CRM）
	 * @param couponReleaseDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CouponReleaseResp>
	 * @date 2022-01-26 09:16:26
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/release")
	ServerResponseEntity<CouponReleaseResp> couponRelease(@RequestBody CouponReleaseDto couponReleaseDto);

	/**
	 * 方法描述：优惠券验证接口
	 * @param couponGetDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CouponCheckResp>
	 * @date 2022-01-26 09:40:04
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/check")
	ServerResponseEntity<CouponCheckResp> couponCheck(@RequestBody CouponGetDto couponGetDto);

	/**
	 * 方法描述：优惠券核销接口
	 * @param couponWriteOffDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-26 10:20:41
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/writeOff")
	ServerResponseEntity couponWriteOff(@RequestBody CouponWriteOffDto couponWriteOffDto);

	/**
	 * 方法描述：优惠券冻结解冻
	 * @param couponFreezeThawDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity<com.mall4j.cloud.api.docking.skq_crm.resp.CouponFreezeThawResp>
	 * @date 2022-01-26 12:56:54
	 */
		@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/freezeThaw")
	ServerResponseEntity<CouponFreezeThawResp> freezeThaw(@RequestBody CouponFreezeThawDto couponFreezeThawDto);

	/**
	 * 方法描述：小程序端创建优惠券推送至CRM
	 * @param couponDetailDTO
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:25:49
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/push")
	ServerResponseEntity<Void> couponPush(@RequestBody CouponDetailDTO couponDetailDTO);

	/**
	 * 方法描述：小程序端更新优惠券推送至CRM
	 * @param couponDetailDTO
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:26:13
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/updatePush")
	ServerResponseEntity<Void> couponUpdatePush(@RequestBody CouponDetailDTO couponDetailDTO);

	/**
	 * 方法描述：优惠券状态同步
	 * @param couponStateUpdateDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2022-01-29 11:36:27
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/state")
	ServerResponseEntity<Void> couponStateUpdate(@RequestBody CouponStateUpdateDto couponStateUpdateDto);
	
	/**
	 * 方法描述：单个优惠券码查询接口
	 * @param couponCode
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2023-04-19 14:00:27
	 */
	@GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/crm/customer/coupon/singleGet")
	ServerResponseEntity<CouponSingleGetResp> couponSingleGet(@RequestParam("couponCode") String couponCode);
}
