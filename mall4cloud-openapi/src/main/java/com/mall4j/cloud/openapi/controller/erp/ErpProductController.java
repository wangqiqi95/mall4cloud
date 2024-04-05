package com.mall4j.cloud.openapi.controller.erp;

import com.mall4j.cloud.api.openapi.skq_erp.dto.ProductPriceSyncDto;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.openapi.service.erp.ErpProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @decription 对接erps商品相关
 * @date 2021/12/29 16:47：04
 */
@RestController
@Api(tags = "对接erps商品相关")
public class ErpProductController {

	@Autowired ErpProductService erpProductService;

//	@PostMapping("/product/productPriceSync")
	@ApiOperation(value = "商品价格同步（门店版）", notes = "商品价格数据同步（中台->小程序），中台把变动的价格同步到小程序。（门店版价格）")
	public ErpResponse productPriceSync(@RequestBody ProductPriceSyncDto productPriceSyncDto) {
		return erpProductService.productPriceSync(productPriceSyncDto);
	}
}
