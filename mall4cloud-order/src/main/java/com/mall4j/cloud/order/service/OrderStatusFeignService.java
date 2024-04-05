package com.mall4j.cloud.order.service;

import com.mall4j.cloud.api.order.dto.OrderDeliveryDto;
import com.mall4j.cloud.api.order.dto.OrderRefundDto;
import com.mall4j.cloud.common.response.ServerResponseEntity;

import java.util.List;

public interface OrderStatusFeignService {

	ServerResponseEntity orderDelivery(OrderDeliveryDto orderDeliveryDto);

	ServerResponseEntity orderDelivery(List<OrderDeliveryDto> orderDeliveryDtos);

	ServerResponseEntity<Void> returnMoney(OrderRefundDto orderRefundParam);
}
