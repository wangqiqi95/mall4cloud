package com.mall4j.cloud.docking.skq_crm.service.impl;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponFreezeThawDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponGetDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponProjectSyncDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponReleaseDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponStateUpdateDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CouponWriteOffDto;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmPageResult;
import com.mall4j.cloud.api.docking.skq_crm.dto.CrmResult;
import com.mall4j.cloud.api.docking.skq_crm.enums.CrmMethod;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponCheckResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponFreezeThawResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponReleaseResp;
import com.mall4j.cloud.api.docking.skq_crm.resp.CouponSingleGetResp;
import com.mall4j.cloud.api.docking.skq_crm.vo.CouponGetVo;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_crm.service.ICrmCouponService;
import com.mall4j.cloud.docking.utils.CrmClients;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("crmCouponService")
public class CrmCouponServiceImpl implements ICrmCouponService {

	@Override
	public ServerResponseEntity<CrmPageResult<List<CouponGetVo>>> couponGet(CouponGetDto couponGetDto) {
		if (null == couponGetDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		Map<String, Object> stringObjectMap = couponGetDto.toMap();
		String result = CrmClients.clients().get(CrmMethod.COUPON_GET.uri(), stringObjectMap);

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-会员优惠券查询接口无响应");
			return fail;
		}

		CrmPageResult<List<CouponGetVo>> crmPageResult = JSONUtil.toBean(result, new TypeReference<CrmPageResult<List<CouponGetVo>>>() {
		}, true);
		String errorMsg = "会员优惠券查询失败";
		if (crmPageResult != null) {
			if (crmPageResult.success()) {
				return ServerResponseEntity.success(crmPageResult);
			}
			errorMsg = crmPageResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity couponProjectSync(CouponProjectSyncDto couponProjectSyncDto) {
		if (null == couponProjectSyncDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_PROJECTSYSN.uri(), JSONObject.toJSONString(couponProjectSyncDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-微信优惠券项目Id同步至CRM接口无响应");
			return fail;
		}
		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "微信优惠券项目Id同步至CRM接口失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity<CouponReleaseResp> couponRelease(CouponReleaseDto couponReleaseDto) {
		if (null == couponReleaseDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_RELEASE.uri(), JSONObject.toJSONString(couponReleaseDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-优惠券发放（小程序—>CRM）接口无响应");
			return fail;
		}
		CrmResult<CouponReleaseResp> crmResult = JSONUtil.toBean(result, new TypeReference<CrmResult<CouponReleaseResp>>() {
		}, true);
		String errorMsg = "优惠券发放（小程序—>CRM）失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success(crmResult.getJsondata());
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity<CouponCheckResp> couponCheck(CouponGetDto couponGetDto) {
		if (null == couponGetDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_CHECK.uri(), JSONObject.toJSONString(couponGetDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-优惠券验证接口无响应");
			return fail;
		}
		CrmResult<CouponCheckResp> crmResult = JSONObject.parseObject(result, new TypeReference<CrmResult<CouponCheckResp>>() {
		});
		String errorMsg = "优惠券验证失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success(crmResult.getJsondata());
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity couponWriteOff(CouponWriteOffDto couponWriteOffDto) {
		if (null == couponWriteOffDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_WRITE_OFF.uri(), JSONObject.toJSONString(couponWriteOffDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-优惠券核销接口无响应");
			return fail;
		}
		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "优惠券核销失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}

	@Override
	public ServerResponseEntity<CouponFreezeThawResp> freezeThaw(CouponFreezeThawDto couponFreezeThawDto) {
		if (null == couponFreezeThawDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_FREEZETHAW.uri(), JSONObject.toJSONString(couponFreezeThawDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-优惠券冻结解冻无响应");
			return fail;
		}
		JSONObject jsonObject = JSONObject.parseObject(result);

		String code = jsonObject.getString("code");
		String errorMsg = jsonObject.getString("message");
		String token = jsonObject.getString("token");
		if ("SUCCESS".equals(code)) {
			if (StringUtils.isNotBlank(token)) {
				CouponFreezeThawResp resp = new CouponFreezeThawResp();
				resp.setToken(token);
				return ServerResponseEntity.success(resp);
			}
			return ServerResponseEntity.success();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
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
		if (null == couponDetailDTO) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_PUSH.uri(), JSONObject.toJSONString(couponDetailDTO));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-小程序券创建同步至CRM接口无响应");
			return fail;
		}

		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "小程序券创建同步至CRM失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
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
		if (null == couponDetailDTO) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_UPDATE_PUSH.uri(), JSONObject.toJSONString(couponDetailDTO));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-小程序券更新同步至CRM接口无响应");
			return fail;
		}

		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "小程序端更新优惠券推送至CRM失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
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
		if (null == couponStateUpdateDto) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		String result = CrmClients.clients().postCrm(CrmMethod.COUPON_STATE_UPDATE.uri(), JSONObject.toJSONString(couponStateUpdateDto));

		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-小程序优惠券状态同步接口无响应");
			return fail;
		}

		CrmResult crmResult = JSONObject.parseObject(result, CrmResult.class);

		String errorMsg = "小程序优惠券状态同步失败";
		if (crmResult != null) {
			if (crmResult.success()) {
				return ServerResponseEntity.success();
			}
			errorMsg = crmResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}
	
	/**
	 * 方法描述：单个优惠券码查询接口
	 *
	 * @param couponCode
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2023-04-19 14:53:27
	 */
	@Override
	public ServerResponseEntity<CouponSingleGetResp> couponSingleGet(String couponCode) {
		
		if (StrUtil.isEmpty(couponCode)) {
			return ServerResponseEntity.fail(ResponseEnum.DATA_INCOMPLETE);
		}
		Map<String, Object> params = new HashMap();
		params.put("coupon_code",couponCode);
		String result = CrmClients.clients().get(CrmMethod.COUPON_SINGLE_GET.uri(),params);
		
		if (StringUtils.isBlank(result)) {
			ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
			fail.setMsg("调用CRM-单个优惠券码查询接口无响应");
			return fail;
		}
		
		CrmResult<CouponSingleGetResp> crmPageResult = JSONUtil.toBean(result,new TypeReference<CrmResult<CouponSingleGetResp>>() {
		}, true);
		String errorMsg = "单个优惠券码查询失败";
		if (crmPageResult != null) {
			if (crmPageResult.success()) {
				return ServerResponseEntity.success(crmPageResult.getJsondata());
			}
			errorMsg = crmPageResult.getMessage();
		}
		ServerResponseEntity fail = ServerResponseEntity.fail(ResponseEnum.DATA_ERROR);
		fail.setMsg(errorMsg);
		return fail;
	}
}
