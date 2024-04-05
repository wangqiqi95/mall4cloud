package com.mall4j.cloud.openapi.controller.crm;

import com.mall4j.cloud.api.coupon.dto.CouponDetailDTO;
import com.mall4j.cloud.api.coupon.dto.CouponSyncDto;
import com.mall4j.cloud.api.openapi.skq_crm.response.CrmResponse;
import com.mall4j.cloud.openapi.service.crm.ICouponService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController extends BaseController {

	@Autowired
	ICouponService couponService;

	@PostMapping("/couponAddSync")
	@ApiOperation(value = "新增优惠券")
	public CrmResponse<List<CouponSyncDto>> save(@RequestBody List<CouponDetailDTO> param) {
		CrmResponse verify = verify();
		return verify.isFail() ? verify : couponService.save(param);
	}

	@PostMapping("/couponUpdateSync")
	@ApiOperation(value = "修改优惠券")
	public CrmResponse<List<CouponSyncDto>> update(@RequestBody List<CouponDetailDTO> param) {
		CrmResponse verify = verify();
		return verify.isFail() ? verify : couponService.update(param);
	}


//	@PostMapping("/ua/couponAddSync")
//	@ApiOperation(value = "test 新增优惠券")
//	public CrmResponse<List<CouponSyncDto>> savetest(@RequestBody List<CouponDetailDTO> param) {
//		return couponService.save(param);
//	}
//
//	@PostMapping("/ua/couponUpdateSync")
//	@ApiOperation(value = "test 修改优惠券")
//	public CrmResponse<List<CouponSyncDto>> updatetest(@RequestBody List<CouponDetailDTO> param) {
//		return couponService.update(param);
//	}
}
