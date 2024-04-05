package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.api.order.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "mall4cloud-order",contextId = "order-status")
public interface OrderStatusFeignClient {

	/**
	 * 方法描述：中台订单发货
	 * @param orderDeliveryDto
	 * @return com.mall4j.cloud.common.response.ServerResponseEntity
	 * @date 2021-12-28 18:10:48
	 */
	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/orderStatus/orderDelivery")
	ServerResponseEntity orderDelivery(@RequestBody OrderDeliveryDto orderDeliveryDto);

	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/orderStatus/orderDeliverys")
	ServerResponseEntity orderDeliverys(@RequestBody List<OrderDeliveryDto> orderDeliveryDtos);

	@PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/orderStatus/return_money")
	ServerResponseEntity<Void> returnMoney(@Valid @RequestBody OrderRefundDto orderRefundParam);
}
