package com.mall4j.cloud.openapi.controller.eto;

import com.mall4j.cloud.api.openapi.skq_erp.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.openapi.service.erp.ErpOrderService;
import com.mall4j.cloud.openapi.service.eto.EtoCouponService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @decription 对接 ETO 优惠券发放
 * @date 2022/11/08 11:04：07
 */
@RestController
@Api(tags = "对接ETO优惠券发放")
public class EtoCouponController {

	@Autowired
	EtoCouponService etoCouponService;

	@ResponseBody
	@PostMapping("/coupon/userReceiveCoupon")
//	@ApiOperation(value = "对接ETO优惠券发放-用户领券", notes = "支持第三方小程序，发放斯凯奇小程序优惠券。ETO需要调用小程序接口发券，增加一个优惠券发放接口。")
	public ErpResponse userReceiveCoupon(@RequestParam("id") Long id,@RequestParam("couponId") Long couponId,@RequestParam("storeId") Long storeId, @RequestParam("userId") Long userId) {
		return etoCouponService.userReceiveCoupon(id, couponId, storeId, userId);
	}

}
