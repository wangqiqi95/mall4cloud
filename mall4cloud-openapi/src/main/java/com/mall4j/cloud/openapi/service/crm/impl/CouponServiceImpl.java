package com.mall4j.cloud.openapi.service.crm.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.coupon.constant.CouponSourceType;
import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.openapi.service.crm.ICouponService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("couponService")
public class CouponServiceImpl implements ICouponService {

	private final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

	@Autowired
	TCouponFeignClient tCouponFeignClient;

	@Override
	public CrmResponse<List<CouponSyncDto>> save(List<CouponDetailDTO> param) {
		long start = System.currentTimeMillis();
		CrmResponse crmResponse = CrmResponse.fail();
		ServerResponseEntity responseEntity = null;
		try {
			if (param == null || CollectionUtil.isEmpty(param)) {
				crmResponse = CrmResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
				return crmResponse;
			}
			for (CouponDetailDTO couponDetailDTO : param) {
				couponDetailDTO.setSourceType(CouponSourceType.CRM_SYNC.getType());
				crmResponse = check(couponDetailDTO, false);
				if (crmResponse.isFail()) {
					return crmResponse;
				}
			}

			if (crmResponse.isSuccess()) {
				// feign调用 user服务更新数据
				responseEntity = tCouponFeignClient.save(param);
				if (responseEntity.isSuccess()) {
					crmResponse = CrmResponse.success(responseEntity.getData());
				} else {
					crmResponse = CrmResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
				}
			}
			return crmResponse;
		} finally {
			logger.info("优惠券添加（crm端）接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", param, responseEntity, crmResponse, System.currentTimeMillis() - start);
		}
	}

	@Override
	public CrmResponse<List<CouponSyncDto>> update(List<CouponDetailDTO> param) {
		long start = System.currentTimeMillis();
		CrmResponse crmResponse = CrmResponse.fail();
		ServerResponseEntity responseEntity = null;
		try {
			if (param == null) {
				crmResponse = CrmResponse.fail(CrmResponseEnum.HTTP_MESSAGE_NOT_READABLE.value(), "请求参数为空");
				return crmResponse;
			}
			for (CouponDetailDTO couponDetailDTO : param) {
				couponDetailDTO.setSourceType(CouponSourceType.CRM_SYNC.getType());
				crmResponse = check(couponDetailDTO, true);
				if (crmResponse.isFail()) {
					return crmResponse;
				}
			}

			if (crmResponse.isSuccess()) {
				// feign调用 user服务更新数据
				responseEntity = tCouponFeignClient.update(param);
				if (responseEntity.isSuccess()) {
					crmResponse = CrmResponse.success(responseEntity.getData());
				} else {
					crmResponse = CrmResponse.fail(CrmResponseEnum.EXCEPTION.value(), responseEntity.getMsg());
				}
			}
			return crmResponse;
		} finally {
			logger.info("优惠券更新（crm端）接收到请求参数：{}，feign调用响应为：{},返回响应为：{}，共耗时：{}", param, responseEntity, crmResponse, System.currentTimeMillis() - start);
		}
	}

	public CrmResponse<Void> check(CouponDetailDTO couponDetailDTO, boolean update) {
		if (update && StringUtils.isBlank(couponDetailDTO.getId())) {
			return CrmResponse.fail("优惠券id不能为空");
		}
		if (StringUtils.isBlank(couponDetailDTO.getName())) {
			return CrmResponse.fail("优惠券名称不能为空");
		}

		if (StringUtils.isNotBlank(couponDetailDTO.getCouponDiscount()) && !verifyBigDeicimal(couponDetailDTO.getCouponDiscount())) {
			return CrmResponse.fail("折扣力度不正确");
		}
		if (StringUtils.isNotBlank(couponDetailDTO.getReduceAmount()) && !verifyBigDeicimal(couponDetailDTO.getReduceAmount())) {
			return CrmResponse.fail("抵用金额不正确");
		}
		if (StringUtils.isNotBlank(couponDetailDTO.getAmountLimitNum()) && !verifyBigDeicimal(couponDetailDTO.getAmountLimitNum())) {
			return CrmResponse.fail("限制金额不正确");
		}
		if (StringUtils.isNotBlank(couponDetailDTO.getMaxDeductionAmount()) && !verifyBigDeicimal(couponDetailDTO.getMaxDeductionAmount())) {
			return CrmResponse.fail("折扣券最大抵扣金额");
		}
		if ((couponDetailDTO.getCommodityLimitType()==1 || couponDetailDTO.getCommodityLimitType()==2)
				&& couponDetailDTO.getCommodityLimitNum()==null) {
			return CrmResponse.fail("商品限制类型为1：不超过/2：不少于时，商品限制件数不能为空");
		}
		if (couponDetailDTO.getCommodityLimitType()==3 && (couponDetailDTO.getCommodityLimitNum()==null || couponDetailDTO.getCommodityLimitMaxNum()==null)) {
			return CrmResponse.fail("商品限制类型为区间时，商品限制件数不能为空");
		}
		if (!update && couponDetailDTO.getPriceType() == null) {
			return CrmResponse.fail("价格类型不能为空");
		}
		return CrmResponse.success();
	}

	private boolean verifyBigDeicimal(String s) {
		if (StringUtils.isNotBlank(s)) {
			BigDecimal bigDecimal = null;
			try {
				bigDecimal = new BigDecimal(s);
			} catch (NumberFormatException e) {
				bigDecimal = null;
			}
			return bigDecimal != null;
		}
		return false;
	}
}
