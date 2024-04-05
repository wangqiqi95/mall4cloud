package com.mall4j.cloud.openapi.service.eto;

import com.mall4j.cloud.api.openapi.skq_erp.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;

public interface EtoCouponService {

	/**
	 * 对接ETO优惠券发放-用户领券
	 * @param id 		领券活动ID
	 * @param couponId	优惠券ID
	 * @param storeId	门店ID
	 * @param userId	用户ID
	 * @return
	 */
	ErpResponse userReceiveCoupon(Long id,Long couponId,Long storeId, Long userId);

}
