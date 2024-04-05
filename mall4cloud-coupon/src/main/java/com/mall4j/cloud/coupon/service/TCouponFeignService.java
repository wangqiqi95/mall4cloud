package com.mall4j.cloud.coupon.service;

import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;

import java.util.List;

public interface TCouponFeignService {

	List<CouponSyncDto> save(List<CouponDetailDTO> param);

	List<CouponSyncDto> update(List<CouponDetailDTO> param);
}
