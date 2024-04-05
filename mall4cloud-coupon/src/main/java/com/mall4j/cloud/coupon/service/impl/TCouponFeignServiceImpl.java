package com.mall4j.cloud.coupon.service.impl;

import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.coupon.service.TCouponFeignService;
import com.mall4j.cloud.coupon.service.TCouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("tCouponFeignService")
@Transactional
public class TCouponFeignServiceImpl implements TCouponFeignService {
	@Resource
	private TCouponService tCouponService;

	@Override
	public List<CouponSyncDto> save(List<CouponDetailDTO> param) {
		List<CouponSyncDto> result = new ArrayList<>(param.size());
		for (com.mall4j.cloud.api.coupon.dto.CouponDetailDTO couponDetailDTO : param) {
			ServerResponseEntity<CouponSyncDto> couponSyncDtoServerResponseEntity = tCouponService.syncAddCoupon(couponDetailDTO);
			if (couponSyncDtoServerResponseEntity.isSuccess()) {
				result.add(couponSyncDtoServerResponseEntity.getData());
			} else {
				throw new LuckException(couponSyncDtoServerResponseEntity.getMsg());
			}
		}
		return result;
	}

	@Override
	public List<CouponSyncDto> update(List<CouponDetailDTO> param) {
		List<CouponSyncDto> result = new ArrayList<>(param.size());
		for (com.mall4j.cloud.api.coupon.dto.CouponDetailDTO couponDetailDTO : param) {
			ServerResponseEntity<CouponSyncDto> couponSyncDtoServerResponseEntity = tCouponService.syncUpdateCoupon(couponDetailDTO);
			if (couponSyncDtoServerResponseEntity.isSuccess()) {
				result.add(couponSyncDtoServerResponseEntity.getData());
				tCouponService.removeByCrmCouponId(couponDetailDTO.getId());
//				tCouponService.removeCacheByCouponId(couponDetailDTO.getCouponid());
				CouponSyncDto data = couponSyncDtoServerResponseEntity.getData();
				tCouponService.removeCacheByCouponId(data.getWechat_coupon_rule_id());
			} else {
				throw new LuckException(couponSyncDtoServerResponseEntity.getMsg());
			}
		}
		return result;
	}
}
