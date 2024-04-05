package com.mall4j.cloud.openapi.controller.erp;

import com.mall4j.cloud.api.openapi.skq_erp.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.openapi.skq_erp.dto.StdCommonReq;
import com.mall4j.cloud.api.openapi.skq_erp.response.ErpResponse;
import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.openapi.service.erp.ErpOrderService;
import com.mall4j.cloud.openapi.service.erp.impl.StdHandlerService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @decription 对接erp订单
 * @date 2021/12/28 18:04：07
 */
@RestController
@Api(tags = "对接erp订单")
public class ErpOrderController {

	@Autowired
	ErpOrderService erpOrderService;

	@Autowired
	HttpServletRequest request;

	@ResponseBody
	@PostMapping("/order/orderShipment")
//	@ApiOperation(value = "线上订单状态变更-发货", notes = "中台完成配送订单发货后，需将发货信息推送至小程序，同时将单号信息同步给小程序")
	public ErpResponse orderDelivery(@RequestBody OrderDeliveryDto orderDeliveryDto) {
		return erpOrderService.orderDelivery(orderDeliveryDto);
	}


	@ResponseBody
	@PostMapping("/order/orderReturnstatus")
//	@ApiOperation(value = "线上退单退货状态变更", notes = "中台收到退货商品后，将收货状态同步给小程序")
	public ErpResponse orderReturnstatus(@RequestBody OrderRefundDto orderRefundParam) {
		return erpOrderService.orderReturnstatus(orderRefundParam);
	}

	@RequestMapping("/api/std/service")
	public StdResult std(StdCommonReq commonReq) {
		return StdHandlerService.getInstance().stdHandler(commonReq, request);
	}

	@RequestMapping("/ua/api/std/service")
	public StdResult stdTest(StdCommonReq commonReq) {
		return StdHandlerService.getInstance().testStdHandler(commonReq, request);
	}
}
