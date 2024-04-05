package com.mall4j.cloud.api.docking.skq_erp.feign;

import com.mall4j.cloud.api.docking.skq_erp.dto.PushOrderDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.PushRefundDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 类描述：std订单接口
 *
 * @date 2022/1/7 18:55：54
 */
@FeignClient(value = "mall4cloud-docking",contextId = "std-order")
public interface StdOrderFeignClient {

	/**
	 * 推订单到中台
	 * @param pushOrderDtos 待推送的订单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/order/pushOrder")
	ServerResponseEntity<String> pushOrder(@RequestBody List<PushOrderDto> pushOrderDtos);

	/**
	 * 推退单到中台
	 * @param pushRefundDtos 待推送的退单
	 * @return 返回requestId  （推单是异步的，这里请求成功后会返回requestId作为本次请求的编号），在回执的时候会用到
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/order/pushRefund")
	ServerResponseEntity<String> pushRefund(@RequestBody List<PushRefundDto> pushRefundDtos);
}
