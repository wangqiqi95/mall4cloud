package com.mall4j.cloud.openapi.service.crm;

import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;

import java.util.List;

public interface ICouponService {

	CrmResponse<List<CouponSyncDto>> save(List<CouponDetailDTO> param);

	CrmResponse<List<CouponSyncDto>> update(List<CouponDetailDTO> param);
}
