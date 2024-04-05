package com.mall4j.cloud.docking.skq_erp.controller;

import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdOrderFeignClient;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.docking.skq_erp.service.IStdOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：对接中台订单相关接口
 *
 * @date 2022/1/7 19:00：45
 */
@RestController
@Api(tags = "对接中台订单相关接口")
public class StdOrderController implements StdOrderFeignClient {

	@Autowired
	IStdOrderService stdOrderService;

	/**
	 * 推订单到中台
	 *
	 * @param pushOrderDto 待推送的订单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@Override
	@ApiOperation(value = "推订单到中台", notes = "推订单到中台，金额单位为元；订单更新类如：订单状态变化也是调此接口推送给中台更新")
	public ServerResponseEntity<String> pushOrder(List<PushOrderDto> pushOrderDto) {
		return stdOrderService.pushOrder(pushOrderDto);
	}

	/**
	 * 推退单到中台
	 *
	 * @param pushRefundDtos 待推送的退单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@Override
	@ApiOperation(value = "推退单到中台", notes = "推订单到中台，金额单位为元；订单更新类如：订单状态（例如：取消退单）变化也是调此接口推送给中台更新")
	public ServerResponseEntity<String> pushRefund(List<PushRefundDto> pushRefundDtos) {
		return stdOrderService.pushRefund(pushRefundDtos);
	}
}
